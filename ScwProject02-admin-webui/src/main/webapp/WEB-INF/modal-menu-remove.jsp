<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<div id="removeMenuModal" class="modal fade" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title">尚筹网系统弹窗</h4>
            </div>
            <div class="modal-body">
                <form class="form-signin" role="form">
                    <div class="form-group has-success has-feedback">
                        <input type="hidden" value="" id="removeMenuId">
                        <span id="removeMessage"></span>
                        <br />
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button id="removeMenuBtn" type="button" class="btn btn-primary">删除</button>

            </div>
        </div>
    </div>
</div>
