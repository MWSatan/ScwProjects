package com.XYF;

import static org.junit.Assert.assertTrue;

import com.XYF.crowd.util.CrowdUtil;
import com.XYF.crowd.util.ResultEntity;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Unit test for simple App.
 */
public class AppTest {
    /**
     * Rigorous Test :-)
     */
    @Test
    public  void randomCode() {
        StringBuffer randomBuffer = new StringBuffer();
        for (int i = 0; i < 4; ++i) {
            int randomCode = (int)(Math.random() * 10);
            randomBuffer.append(randomCode);
            System.out.println(randomCode);

        }
        System.out.println(randomBuffer.toString());
    }

    @Test
    public  void testUpload() throws FileNotFoundException {
        InputStream input= new FileInputStream("timg.jfif");
        ResultEntity<String> resultEntity = CrowdUtil.uploadFileToOss("oss-cn-hangzhou.aliyuncs.com","xyf01","LTAI5t67j23heWgPWo2szvdW","GAEZXo7jIQ33KtwFjhr2HtUesgXMnK",
                "xyf01.oss-cn-hangzhou.aliyuncs.com",input,"timg.jfif");
        System.out.println(resultEntity);
    }
}
