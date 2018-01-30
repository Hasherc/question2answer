<#include "header.ftl">
<link rel="stylesheet" media="all" href="../styles/letter.css">
<div id="main">
    <div class="zg-wrap zu-main clearfix ">
        <ul class="letter-chatlist">
        <#list vos>
            <#items as vo>
                <li id="msg-item-4009580">
                    <a href="/user/${vo.user.id}" class="list-head">
                        <img alt="头像" src="${vo.user.headUrl}">
                    </a>
                    <div class="tooltip fade right in">
                        <div class="tooltip-arrow"></div>
                        <div class="tooltip-inner letter-chat clearfix">
                            <div class="letter-info">
                                <p class="letter-time">${vo.message.createdDate?datetime}</p>
                                <a href="javascript:void(0);" id="del-link" name="4009580">删除</a>
                            </div>
                            <p class="chat-content">
                            ${vo.message.content}
                            </p>
                        </div>
                    </div>
                </li>
            </#items>
        </#list>


        </ul>
    </div>

</div>
<#include "js.ftl">
<#include "footer.ftl">