package com.XYF.crowd.handler;

import com.XYF.crowd.api.MySqlRemoteService;
import com.XYF.crowd.config.OSSProperties;
import com.XYF.crowd.constant.CrowdConstant;
import com.XYF.crowd.entity.vo.*;
import com.XYF.crowd.util.CrowdUtil;
import com.XYF.crowd.util.ResultEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @username 熊一飞
 */

@Controller
public class ProjectConsumerHandler {

    @Autowired
    private OSSProperties ossProperties;

    @Autowired
    private MySqlRemoteService mySqlRemoteService;

    private Logger logger = LoggerFactory.getLogger(ProjectConsumerHandler.class);


    @RequestMapping("/portal/show/project/detail/{projectId}")
    public String getProjectDetail(@PathVariable("projectId") Integer projectId, ModelMap modelMap) {

//        调用api接口
        ResultEntity<DetailProjectVO> detailProjectVOResultEntity = mySqlRemoteService.getDetailProjectData(projectId);

        if (ObjectUtils.nullSafeEquals(ResultEntity.SUCCESS, detailProjectVOResultEntity.getResult())) {
            DetailProjectVO detailProjectVO = detailProjectVOResultEntity.getData();
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_DETAIL_PROJECT, detailProjectVO);
        }

        return "project-show-detail";
    }

    @RequestMapping("/create/confirm")
    public String saveConfirm(ModelMap modelMap, MemberConfirmInfoVO confirmInfoVO, HttpSession session) {
//        1、取出redis保存的ProjectVO对象
        ProjectVO projectVO = (ProjectVO) session.getAttribute(CrowdConstant.MESSAGE_ATTR_NAME_TEMP_PROJECTVO);

        if (projectVO == null) {
            throw new RuntimeException(CrowdConstant.MESSAGE_TEMP_PROJECT_MISSING);
        }

//       2、将confirmVO对象保存至ProjectVO对象中
        projectVO.setMemberConfirmInfoVO(confirmInfoVO);

//       3、 取出存在redis中的MemberLoginVO对象
        MemberLoginVO memberLoginVO = (MemberLoginVO) session.getAttribute(CrowdConstant.ATTR_NAME_MEMBER);

        Integer memberId = memberLoginVO.getId();

        logger.error(projectVO.toString());


//       4、调用MySqlRemoteService来通过id来保存ProjectVO对象
        ResultEntity<String> saveProjectVOResultEntity = mySqlRemoteService.saveProjectVORemote(projectVO, memberId);

//        先判断失败，因为还有后续操作，失败后返回confirm页面
        if (!ObjectUtils.nullSafeEquals(saveProjectVOResultEntity.getResult(), ResultEntity.SUCCESS)) {
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, saveProjectVOResultEntity.getMessage());
            return "project-confirm";
        }

//        将临时的ProjectVO对象移除，保存完成后就没有存在的必要
        session.removeAttribute(CrowdConstant.MESSAGE_ATTR_NAME_TEMP_PROJECTVO);

//        重定向到成功页面
        return "redirect:http://www.crowd.com/project/success/project/page.html";
    }

    /**
     * 保存return页面发生过来的表单，并更新存储在SpringSession域中的returnVO数据
     *
     * @return
     */
    @RequestMapping("/create/save/return.json")
    @ResponseBody
    public ResultEntity<String> saveReturn(ReturnVO returnVO, HttpSession session) {


        try {


//        1.取出redis中存储的projectVO对象
            ProjectVO projectVO = (ProjectVO) session.getAttribute(CrowdConstant.MESSAGE_ATTR_NAME_TEMP_PROJECTVO);

//        2、判断redis中的projectVO对象是否存在
            if (projectVO == null) {
                return ResultEntity.failWithMessage(CrowdConstant.MESSAGE_TEMP_PROJECT_MISSING);
            }

//        3、从ProjectVO对象中获取存储returnVO对象的集合

            List<ReturnVO> returnVOList = projectVO.getReturnVOList();

//        4.判断是否有效,因为保存第一条之前是无效的，所以需要判断一下
            if (returnVOList == null || returnVOList.size() == 0) {
//            重新初始化集合
                returnVOList = new ArrayList<ReturnVO>();

//            将该集合设置到ProjectVO对象中
                projectVO.setReturnVOList(returnVOList);
            }

//        5.添加returnVO至该集合中
            returnVOList.add(returnVO);

            logger.error(projectVO.toString());


//        6.将更新后的projectVO存入SESSION与中，并返回成功的信息
            session.setAttribute(CrowdConstant.MESSAGE_ATTR_NAME_TEMP_PROJECTVO, projectVO);

//        返回成功信息
            return ResultEntity.successWithoutData();

        } catch (Exception e) {

            return ResultEntity.failWithMessage(e.getMessage());

        }

    }


    /**
     * 单独通过ajax先上传可return页面的头图
     * 然后放至formData.append("returnPicture", file);
     * file为文件名
     *
     * @param returnPicture
     * @return
     * @throws IOException
     */
    @RequestMapping("/create/upload/return/picture.json")
    @ResponseBody
    public ResultEntity<String> uploadReturnPicture(MultipartFile returnPicture) throws IOException {


        BufferedImage sourceImg = javax.imageio.ImageIO.read(returnPicture.getInputStream());
        if (sourceImg.getHeight() > 760 || sourceImg.getWidth() > 510 || sourceImg.getHeight() == 0 || sourceImg.getWidth() == 0) {
            return ResultEntity.failWithMessage("图片尺寸不对");
        } else {

//        1.执行文件上传
            ResultEntity<String> uploadReturnEntity = CrowdUtil.uploadFileToOss(
                    ossProperties.getEndPoint(),
                    ossProperties.getBucketName(),
                    ossProperties.getAccessKeyId(),
                    ossProperties.getAccessKeySecret(),
                    ossProperties.getBucketDomain(),
                    returnPicture.getInputStream(),
                    returnPicture.getOriginalFilename()
            );
//        返回上传的结果
            return uploadReturnEntity;

        }

    }

    /**
     * @param projectVo         接收除了上传图片之外的其他普通数据
     *                          MultipartFile是spring类型，代表HTML中form data方式上传的文件，包含二进制数据+文件名称。
     * @param headerPicture     接收上传的头图
     * @param detailPictureList 接收上传的详情图片
     * @param session           将收集的一部分数据对象存入session域
     * @param modelMap          返回上传失败的提示消息
     * @return
     */
    @RequestMapping("/create/project/information.html")
    public String saveProjectBasicInfo(
            ProjectVO projectVo,
            MultipartFile headerPicture,
            List<MultipartFile> detailPictureList,
            HttpSession session,
            ModelMap modelMap
    ) throws IOException {


        //        判断上传打的图片尺寸
        BufferedImage headImg = javax.imageio.ImageIO.read(headerPicture.getInputStream());

        if (headImg.getHeight() > 740 || headImg.getWidth() > 430 || headImg.getHeight() == 0 || headImg.getWidth() == 0) {
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_HEADER_PICTURE_SIZE_ERROR);
            return "project-launch";
        }


        //            头图为空

        if (headerPicture.isEmpty()) {
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_HEADER_PICTURE_IS_NULL);
            return "project-launch";
        }

//        1.如果头图不为空则上传oss服务器中
        ResultEntity<String> uploadHeaderPictureResultEntity = CrowdUtil.uploadFileToOss(
                ossProperties.getEndPoint(),
                ossProperties.getBucketName(),
                ossProperties.getAccessKeyId(),
                ossProperties.getAccessKeySecret(),
                ossProperties.getBucketDomain(),
                headerPicture.getInputStream(),
                headerPicture.getOriginalFilename()
        );


//            2、 判断是否上传成功
        String headerPictureResult = uploadHeaderPictureResultEntity.getResult();

        if (ObjectUtils.nullSafeEquals(headerPictureResult, ResultEntity.SUCCESS)) {

//              3、从oss服务器中获取返回的路径
            String headerPicturePath = uploadHeaderPictureResultEntity.getData();

//              4、将上传的路径存入vo对象中
            projectVo.setHeaderPicturePath(headerPicturePath);

        } else {
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_HEADER_PICTURE_SIZE_ERROR);
            return "project-launch";
        }


//        用来保存详情图片路径集合
        List<String> detailPathList = new ArrayList<String>();

//        5.遍历detailMultipartFileList
        for (MultipartFile detailPicture : detailPictureList) {
            BufferedImage detailImg = javax.imageio.ImageIO.read(detailPicture.getInputStream());

            if (detailImg.getHeight() > 500 || detailImg.getWidth() > 500 || detailImg.getHeight() == 0 || detailImg.getWidth() == 0) {
                modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_DERAIL_PICTURE_SIZE_ERROR);
                return "project-launch";
            }


            //6、判断是否为空
            if (detailPicture.isEmpty()) {
                modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_DETAIL_PICTURE_IS_NULL);
                return "project-launch";
            }
//                7、上传至oss服务器中
            ResultEntity<String> uploadDetailPictureResultEntity = CrowdUtil.uploadFileToOss(
                    ossProperties.getEndPoint(),
                    ossProperties.getBucketName(),
                    ossProperties.getAccessKeyId(),
                    ossProperties.getAccessKeySecret(),
                    ossProperties.getBucketDomain(),
                    detailPicture.getInputStream(),
                    detailPicture.getOriginalFilename()
            );

//              8、判断是否上传成功
            String detailPictureResult = uploadDetailPictureResultEntity.getResult();
            if (ObjectUtils.nullSafeEquals(detailPictureResult, ResultEntity.SUCCESS)) {

//              9、从oss服务器中获取返回的路径
                String detailPicturePath = uploadDetailPictureResultEntity.getData();

//              10、将上传的详细图片路径存入集合中
                detailPathList.add(detailPicturePath);
            } else {
                modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_DETAIL_PICTURE_FAIL);
                return "project-launch";
            }


        }

//        11.判断List集合是否有效
        if (detailPathList == null || detailPathList.size() == 0) {
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_DETAIL_PICTURE_IS_NULL);
            return "project-launch";
        } else {
            //  11、有效就将详情图片路径集合放入vo对象中
            projectVo.setDetailPicturePathList(detailPathList);
        }


//        10.将ProjectVo对象存入session域
        session.setAttribute(CrowdConstant.MESSAGE_ATTR_NAME_TEMP_PROJECTVO, projectVo);

//        由于在路由配置了/project/** 所以发送请求需要加上这个project
        return "redirect:http://www.crowd.com/project/return/info/page.html";

    }

}
