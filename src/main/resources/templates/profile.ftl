<#include "header.ftl">
<link rel="stylesheet" href="../styles/index.css">
<link rel="stylesheet" href="../styles/detail.css">
<div class="zg-wrap zu-main clearfix " role="main">

    <div class="zm-profile-section-wrap zm-profile-followee-page">
        <div class="zm-profile-section-list">
            <div id="zh-profile-follows-list">
                <div class="zh-general-list clearfix">
                    <div class="zm-profile-card zm-profile-section-item zg-clear no-hovercard">
                        <div class="zg-right">
                        <#if followed == true>
                            <button class="zg-btn zg-btn-unfollow zm-rich-follow-btn small nth-0
                                    js-follow-user" data-status="1" data-id="${profileUser.id}">取消关注
                            </button>
                        <#else>
                            <button class="zg-btn zg-btn-follow zm-rich-follow-btn small nth-0
                                    js-follow-user" data-id="${profileUser.id}">关注
                            </button>
                        </#if>
                        </div>
                        <a title="Barty" class="zm-item-link-avatar" href="/user/${profileUser.id}">
                            <img src="${profileUser.headUrl}" class="zm-item-img-avatar">
                        </a>
                        <div class="zm-list-content-medium">
                            <h2 class="zm-list-content-title"><a data-tip="p$t$buaab    arty"
                                                                 href="/user/${profileUser.id}"
                                                                 class="zg-link">${profileUser.name}</a></h2>

                            <!-- <div class="zg-big-gray">计蒜客教研首席打杂</div> -->
                            <div class="details zg-gray">
                                <a target="_blank" href="/user/${profileUser.id}/followers"
                                   class="zg-link-gray-normal">${followerCount}粉丝</a>
                                /
                                <a target="_blank" href="/user/${profileUser.id}/followees"
                                   class="zg-link-gray-normal">${followeeCount}关注</a>
                                /
                                <a target="_blank" href="#" class="zg-link-gray-normal">${commentCount} 回答</a>
                                /
                                <a target="_blank" href="#" class="zg-link-gray-normal">548 赞同</a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

    </div>
    <div class="zu-main-content">
        <div class="zu-main-content-inner">
            <div class="zg-section" id="zh-home-list-title">
                <i class="zg-icon zg-icon-feedlist"></i>最新动态
                <span class="zg-right zm-noti-cleaner-setting" style="list-style:none">
                        <a href="https://nowcoder.com/settings/filter" class="zg-link-gray-normal">
                            <i class="zg-icon zg-icon-settings"></i>设置
                        </a>
                    </span>
            </div>
            <div class="zu-main-feed-con navigable" data-feedtype="topstory" id="zh-question-list">
                <div id="js-home-feed-list" class="zh-general-list topstory clearfix">
                <#list vos>
                    <#items as vo>
                        <div class="feed-item folding feed-item-hook feed-item-1" feed-item-p="" data-type="p"
                             id="feed-1" data-za-module="FeedItem" data-za-index="">
                            <meta itemprop="ZReactor" data-id="113477"
                                  data-meta="{&quot;source_type&quot;: &quot;promotion_article&quot;, &quot;voteups&quot;: 1082, &quot;comments&quot;: 100, &quot;source&quot;: []}">
                            <div class="feed-item-inner">
                                <div class="avatar">
                                    <a title="${vo.user.name}" data-tip="p$t$zhao-yong-feng" class="zm-item-link-avatar"
                                       target="_blank" href="/user/${vo.user.id}">
                                        <img src="${vo.user.headUrl}" class="zm-item-img-avatar"></a>
                                </div>
                                <div class="feed-main">
                                    <div class="feed-content" data-za-module="PostItem">
                                        <meta itemprop="post-id" content="113477">
                                        <meta itemprop="post-url-token" content="19831487">
                                        <h2 class="feed-title">
                                            <a target="_blank" class="post-link"
                                               href="/question/${vo.question.id}">${vo.question.title}</a></h2>
                                        <div class="feed-question-detail-item">
                                            <div class="question-description-plain zm-editable-content"></div>
                                        </div>
                                        <div class="entry-body post-body js-collapse-body">
                                            <div class="zm-item-vote">
                                                <a class="zm-item-vote-count js-expand js-vote-count"
                                                   href="javascript:;" data-bind-votecount="">1082</a>
                                            </div>
                                            <div class="zm-item-answer-author-info">
                                                <a class="author-link" data-tip="p$b$amuro1230" target="_blank"
                                                   href="/user/${vo.user.id}">
                                                ${vo.user.name}
                                                </a>
                                                ，${vo.question.createdDate?date}
                                            </div>

                                            <div class="zm-item-vote-info" data-votecount="1082"
                                                 data-za-module="VoteInfo">
                                                    <span class="voters text">
                                                        <a href="#" class="more text">
                                                            <span class="js-voteCount">1082</span>&nbsp;人赞同
                                                        </a>
                                                    </span>
                                            </div>
                                            <div class="zm-item-rich-text expandable js-collapse-body"
                                                 data-resourceid="123114" data-action="/answer/content"
                                                 data-author-name="李淼"
                                                 data-entry-url="/question/19857995/answer/13174385">
                                                <div class="zh-summary summary clearfix">${vo.question.content}</div>
                                            </div>


                                        </div>
                                        <div class="feed-meta">
                                            <div class="zm-item-meta meta clearfix js-contentActions">
                                                <div class="zm-meta-panel">
                                                    <a data-follow="c:link" class="zg-follow meta-item" href="#"
                                                       id="cl-2180">
                                                        <i class="z-icon-follow"></i>关注专栏</a>
                                                    <a href="/question/${vo.question.id}"
                                                       class="meta-item toggle-comment js-toggleCommentBox">
                                                        <i class="z-icon-comment"></i>${vo.question.commentCount}
                                                        条评论</a>
                                                    <a href="#"
                                                       class="meta-item zu-autohide js-share goog-inline-block goog-menu-button"
                                                       role="button" aria-expanded="false" tabindex="0"
                                                       aria-haspopup="true" style="-webkit-user-select: none;">
                                                        <div class="goog-inline-block goog-menu-button-outer-box">
                                                            <div class="goog-inline-block goog-menu-button-inner-box">
                                                                <div class="goog-inline-block goog-menu-button-caption">
                                                                    <i class="z-icon-share"></i>分享
                                                                </div>
                                                                <div class="goog-inline-block goog-menu-button-dropdown">
                                                                    &nbsp;
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </a>
                                                    <span class="zg-bull zu-autohide">•</span>
                                                    <a href="#" class="meta-item zu-autohide js-report">举报</a>
                                                    <button class="meta-item item-collapse js-collapse">
                                                        <i class="z-icon-fold"></i>收起
                                                    </button>
                                                </div>
                                            </div>
                                            <a href="#" class="ignore zu-autohide" name="dislike"
                                               data-tip="s$b$不感兴趣"></a>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="undo-dislike-options" data-item_id="1">此内容将不会在动态中再次显示
                                <span class="zg-bull">•</span>
                                <a href="#" class="meta-item revert">撤销</a>
                                <a href="#" class="ignore zu-autohide close"></a>
                            </div>
                        </div>
                    </#items>
                </#list>
                </div>
                <a href="javascript:;" id="zh-load-more" class="zg-btn-white zg-r3px zu-button-more">更多</a>
            </div>
        </div>
    </div>
</div>
<#include "js.ftl">
<#include "footer.ftl">