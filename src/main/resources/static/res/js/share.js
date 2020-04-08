/**
 * @author zhanghongliang
 * @version 2017年11月24日
 * @desc
 */

function getWxConfig(shareTitle, shareDesc, imgUrl) {
    var _url = location.href.split('#')[0];
    var and = '?';
    if (_url.indexOf('?') > 0) {
        and = '&';
    }
    $.ajax({
        url: 'http://wxpay.medlive.cn/api.php?c=user&action=getSignPackage&domain=test',
        dataType: 'jsonp',
        success: function (o) {
            wx.config({
                debug: false,
                appId: o.data.appId,
                timestamp: o.data.timestamp,
                nonceStr: o.data.nonceStr,
                signature: o.data.signature,
                jsApiList: [
                    'checkJsApi',
                    'onMenuShareTimeline',
                    'onMenuShareAppMessage']
            });

            //通过ready接口处理成功验证
            wx.ready(function () {
                wx.onMenuShareTimeline({
                    debug: false,
                    title: shareDesc, // 分享标题
                    link: _url + and + 'source=timeline',
                    imgUrl: imgUrl,
                    success: function () {
                        // 用户确认分享后执行的回调函数
                        //$.alert("分享成功");
                    },
                    cancel: function () {
                        // 用户取消分享后执行的回调函数
                        //$.alert("分享失败");
                    }
                });
                //分享给朋友
                wx.onMenuShareAppMessage({
                    debug: false,
                    title: shareTitle, // 分享标题
                    desc: shareDesc, // 分享描述
                    link: _url + and + 'source=friend', // 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致
                    imgUrl: imgUrl, // 分享图标
                    type: '', // 分享类型,music、video或link，不填默认为link
                    dataUrl: '', // 如果type是music或video，则要提供数据链接，默认为空
                    success: function () {
                        // 用户确认分享后执行的回调函数
                        //$.alert("分享成功!");
                    },
                    cancel: function () {
                        //$.alert("取消成功!");
                        // 用户取消分享后执行的回调函数
                    }
                });
            });
        }
    });
}

function shareCommon(title, shareDesc) {
    getWxConfig(title, shareDesc, "http://huodong.medlive.cn/nhblood/res/img/slt.jpg");
}
