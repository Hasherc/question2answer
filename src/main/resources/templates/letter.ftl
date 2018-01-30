<#include "header.ftl"/>

<link rel="stylesheet" media="all" href="../styles/letter.css">
<div id="main">
    <div class="zg-wrap zu-main clearfix ">
    <#list vos>
        <#items as vo>
            <ul class="letter-list">
                <li id="conversation-item-10005_622873">
                    <a class="letter-link" href="/msg/${vo.message.conversationId}"></a>
                    <div class="letter-info">
                        <span class="l-time">${vo.message.createdDate?datetime}</span>
                        <div class="l-operate-bar">
                            <a href="javascript:void(0);" class="sns-action-del" data-id="10005_622873">
                                删除
                            </a>
                            <a href="/msg/${vo.message.conversationId}">
                                共${vo.message.id}条会话
                            </a>
                        </div>
                    </div>
                    <div class="chat-headbox">
                        <span class="msg-num">
                        ${vo.unReadCount}
                        </span>
                        <a href="/user/${vo.user.id}" class="list-head">
                            <img alt="头像" src="${vo.user.headUrl}">
                        </a>
                    </div>
                    <div class="letter-detail">

                        <p class="letter-brief">
                            <a href="/msg/${vo.message.conversationId}">
                            ${vo.message.content}
                            </a>
                        </p>
                    </div>
                </li>

            </ul>
        </#items>
    </#list>


    </div>

</div>

<#include "js.ftl">
<#include "footer.ftl">