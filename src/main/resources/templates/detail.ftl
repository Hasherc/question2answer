<#include "header.ftl">
<link rel="stylesheet" href="../styles/detail.css">
<div class="zg-wrap zu-main clearfix with-indention-votebar" id="zh-single-question-page">
    <div class="zu-main-content">
        <div class="zu-main-content-inner">
            <meta itemprop="isTopQuestion" content="false">
            <meta itemprop="visitsCount" content="402">

            <div id="zh-question-title" data-editable="true" class="zm-editable-status-normal">
                <h2 class="zm-item-title">
                    <span class="zm-editable-content">${question.title}</span>
                </h2>
            </div>

            <div id="zh-question-detail" class="zm-item-rich-text zm-editable-status-normal">
                <div class="zm-editable-content">${question.content}</div>
            </div>

            <div class="zm-side-section">
                <div class="zm-side-section-inner" id="zh-question-side-header-wrap">
                <#if followed == true>
                    <button class="follow-button zg-follow zg-btn-white js-follow-question" data-id="${question.id}"
                            data-status="1">
                        取消关注
                    </button>
                <#else >
                    <button class="follow-button zg-follow zg-btn-green js-follow-question" data-id="${question.id}">
                        关注问题
                    </button>
                </#if>
                    <div class="zh-question-followers-sidebar">
                        <div class="zg-gray-normal">
                            <a href="javascript:void(0);"><strong class="js-user-count">${followUsers?size}</strong></a>人关注该问题
                        </div>
                        <div class="list zu-small-avatar-list zg-clear js-user-list">
                        <#list followUsers>
                            <#items as vo>
                                <a class="zm-item-link-avatar js-user-${vo.id}" href="/user/${vo.id}"
                                   data-original_title="${vo.name}">
                                    <img src="${vo.headUrl}" class="zm-item-img-avatar">
                                </a>
                            </#items>
                        </#list>

                        </div>
                    </div>
                </div>
            </div>

            <div id="zh-question-answer-wrap" data-pagesize="10" class="zh-question-answer-wrapper navigable"
                 data-widget="navigable" data-navigable-options="{&quot;items&quot;: &quot;&gt;.zm-item-answer&quot;}"
                 data-init="{&quot;params&quot;: {&quot;url_token&quot;: 36301524, &quot;pagesize&quot;: 10, &quot;offset&quot;: 0}, &quot;nodename&quot;: &quot;QuestionAnswerListV2&quot;}">

            <#list comments>
                <#items as vo>
                    <div class="zm-item-answer  zm-item-expanded js-comment">
                        <link itemprop="url" href="">
                        <meta itemprop="answer-id" content="22162611">
                        <meta itemprop="answer-url-token" content="66862039">
                        <a class="zg-anchor-hidden" name="answer-22162611"></a>

                        <div class="zm-votebar goog-scrollfloater js-vote" data-id="${vo.comment.id}">

                            <#if 0 < vo.liked>
                            <button class="up js-like pressed" title="赞同">
                            <#else>
                            <button class="up js-like" title="赞同">
                            </#if>
                            <i class="icon vote-arrow"></i>
                            <span class="count js-voteCount">${vo.likeCount}</span>
                            <span class="label sr-only">赞同</span>
                        </button>

                            <#if vo.liked < 0>
                            <button class="down js-dislike pressed" title="反对，不会显示你的姓名">
                            <#else>
                            <button class="down js-dislike" title="反对，不会显示你的姓名">
                            </#if>
                            <i class="icon vote-arrow"></i>
                            <span class="label sr-only">反对，不会显示你的姓名</span>
                        </button>
                        </div>
                        <div class="answer-head">
                            <div class="zm-item-answer-author-info">
                                <a class="zm-item-link-avatar avatar-link" href="" target="_blank">
                                    <img src="${vo.user.headUrl}" class="zm-list-avatar avatar">
                                </a>
                                <a class="author-link" target="_blank" href="/user/${vo.user.id}">${vo.user.name}</a>
                            </div>
                            <div class="zm-item-vote-info">
                                    <span class="voters text">
                                        <a href="" class="more text">
                                            <span class="js-voteCount">${vo.likeCount}
                                            </span>&nbsp;人赞同
                                        </a>
                                    </span>
                            </div>
                        </div>
                        <div class="zm-item-rich-text expandable js-collapse-body">
                            <div class="zm-editable-content clearfix">
                            ${vo.comment.content}
                            </div>
                        </div>
                        <a class="zg-anchor-hidden ac" name="22162611-comment"></a>
                        <div class="zm-item-meta answer-actions clearfix js-contentActions">
                            <div class="zm-meta-panel">
                                <a itemprop="url"
                                   class="answer-date-link meta-item">发布于 ${vo.comment.createdDate?date}</a>
                                <a name="addcomment" class="meta-item toggle-comment js-toggleCommentBox">
                                    <i class="z-icon-comment"></i>4 条评论</a>
                                <a class="meta-item zu-autohide js-thank">
                                    <i class="z-icon-thank"></i>感谢</a>
                            </div>
                        </div>
                    </div>
                </#items>
            </#list>
            </div>
            <a name="draft"></a>
            <form action="/addComment" method="post" id="commentform">
                <input type="hidden" name="questionId" value="${question.id}"/>
                <div id="zh-question-answer-form-wrap" class="zh-question-answer-form-wrap">
                    <div class="zm-editable-editor-wrap" style="">
                        <div class="zm-editable-editor-outer">
                            <div class="zm-editable-editor-field-wrap">
                                <textarea name="content" id="content" class="zm-editable-editor-field-element editable"
                                          style="font-style: italic;width:100%;"></textarea>
                            </div>
                        </div>

                        <div class="zm-command clearfix">
                                    <span class=" zg-right">
                                    <button type="submit" class="submit-button zg-btn-blue">发布回答</button></span>
                        </div>
                    </div>
                </div>
            </form>

        </div>
    </div>
</div>
<#include "js.ftl">
<script type="text/javascript" src="/scripts/main/site/detail.js"></script>
<#include "footer.ftl">