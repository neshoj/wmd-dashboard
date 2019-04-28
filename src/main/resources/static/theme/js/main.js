/*!
 * amaretti v1.3.0 (http://foxythemes.net/themes/amaretti)
 * Copyright 2014-2017 Foxy Themes all rights reserved 
 */
function FastClick(a, b) {
    "use strict";

    function c(a, b) {
        return function () {
            return a.apply(b, arguments)
        }
    }

    var d;
    if (b = b || {}, this.trackingClick = !1, this.trackingClickStart = 0, this.targetElement = null, this.touchStartX = 0, this.touchStartY = 0, this.lastTouchIdentifier = 0, this.touchBoundary = b.touchBoundary || 10, this.layer = a, this.tapDelay = b.tapDelay || 200, !FastClick.notNeeded(a)) {
        for (var e = ["onMouse", "onClick", "onTouchStart", "onTouchMove", "onTouchEnd", "onTouchCancel"], f = this, g = 0, h = e.length; g < h; g++) f[e[g]] = c(f[e[g]], f);
        deviceIsAndroid && (a.addEventListener("mouseover", this.onMouse, !0), a.addEventListener("mousedown", this.onMouse, !0), a.addEventListener("mouseup", this.onMouse, !0)), a.addEventListener("click", this.onClick, !0), a.addEventListener("touchstart", this.onTouchStart, !1), a.addEventListener("touchmove", this.onTouchMove, !1), a.addEventListener("touchend", this.onTouchEnd, !1), a.addEventListener("touchcancel", this.onTouchCancel, !1), Event.prototype.stopImmediatePropagation || (a.removeEventListener = function (b, c, d) {
            var e = Node.prototype.removeEventListener;
            "click" === b ? e.call(a, b, c.hijacked || c, d) : e.call(a, b, c, d)
        }, a.addEventListener = function (b, c, d) {
            var e = Node.prototype.addEventListener;
            "click" === b ? e.call(a, b, c.hijacked || (c.hijacked = function (a) {
                a.propagationStopped || c(a)
            }), d) : e.call(a, b, c, d)
        }), "function" == typeof a.onclick && (d = a.onclick, a.addEventListener("click", function (a) {
            d(a)
        }, !1), a.onclick = null)
    }
}

var App = function () {
        "use strict";

        function a(a) {
            var b = $("<div>", {
                    class: a
                }).appendTo("body"),
                c = b.css("background-color");
            return b.remove(), c
        }

        function b() {
            function a(a) {
                var b = $(".sidebar-elements > li", k);
                "undefined" != typeof a && (b = a), $.each(b, function (a, b) {
                    var c = $(this).find("> a span").html(),
                        d = $(this).find("> ul"),
                        e = $("> li", d),
                        c = $('<li class="title">' + c + "</li>"),
                        f = $('<li class="nav-items"><div class="am-scroller nano"><div class="content nano-content"><ul></ul></div></div></li>');
                    !d.find("> li.title").length > 0 && (d.prepend(c), e.appendTo(f.find(".content ul")), f.appendTo(d))
                })
            }

            function b() {
                i.addClass(m.openLeftSidebarClass + " " + m.transitionClass), o = !0
            }

            function c() {
                i.removeClass(m.openLeftSidebarClass).addClass(m.transitionClass), d()
            }

            function e(b, c, d) {
                var e = $(b).parent(),
                    f = e.find(".am-scroller"),
                    g = e.find("> ul"),
                    h = e.hasClass("open");
                $.isXs() || ($("ul.visible", k).removeClass("visible").parent().removeClass("open"), (!d || d && !h) && (e.addClass("open"), g.addClass("visible")), g.removeClass("hide")), f.nanoScroller({
                    destroy: !0
                }), f.nanoScroller(), d ? e.hasClass("parent") && c.preventDefault() : m.syncSubMenuOnHover && a(e)
            }

            var f = $(".sidebar-elements > li > a", k),
                g = !(!k.hasClass(m.openLeftSidebarOnClickClass) && !m.openLeftSidebarOnClick);
            if (a(), $(".am-toggle-left-sidebar").on("click", function (a) {
                o && i.hasClass(m.openLeftSidebarClass) ? c() : o || b(), a.stopPropagation(), a.preventDefault()
            }), $(document).on("touchstart mousedown", function (a) {
                !$(a.target).closest(k).length && i.hasClass(m.openLeftSidebarClass) ? c() : $(a.target).closest(k).length || $.isXs() || $("ul.visible", k).removeClass("visible").parent().removeClass("open")
            }), g ? f.on("click", function (a) {
                $.isXs() || e(this, a, !0)
            }) : (f.on("mouseover", function (a) {
                e(this, a, !1)
            }), f.on("touchstart", function (a) {
                $.isXs() || e(this, a, !0)
            }), f.on("mouseleave", function () {
                var a = $(this),
                    b = a.parent(),
                    c = b.find("> ul");
                $.isXs() || (c.length > 0 ? setTimeout(function () {
                    c.is(":hover") ? c.on("mouseleave", function () {
                        setTimeout(function () {
                            a.is(":hover") || (c.removeClass("visible"), b.removeClass("open"), c.off("mouseleave"))
                        }, 300)
                    }) : (c.removeClass("visible"), b.removeClass("open"))
                }, 300) : b.removeClass("open"))
            })), $(".sidebar-elements li a", k).on("click", function (a) {
                if ($.isXs()) {
                    var b, c = $(this),
                        d = m.leftSidebarSlideSpeed,
                        e = c.parent(),
                        f = c.next();
                    b = e.siblings(".open"), b && b.find("> ul:visible").slideUp({
                        duration: d,
                        complete: function () {
                            b.toggleClass("open"), $(this).removeAttr("style").removeClass("visible")
                        }
                    }), e.hasClass("open") ? f.slideUp({
                        duration: d,
                        complete: function () {
                            e.toggleClass("open"), $(this).removeAttr("style").removeClass("visible")
                        }
                    }) : f.slideDown({
                        duration: d,
                        complete: function () {
                            e.toggleClass("open"), $(this).removeAttr("style").addClass("visible")
                        }
                    }), c.next().is("ul") && a.preventDefault()
                } else if (!g) {
                    var h = $(".sidebar-elements > li > ul:visible", k);
                    h.addClass("hide")
                }
            }), $("li.active", k).parents(".parent").addClass("active"), j.hasClass("am-fixed-sidebar")) {
                var h = $(".am-left-sidebar > .content");
                h.wrap('<div class="am-scroller nano"></div>'), h.addClass("nano-content"), h.parent().nanoScroller()
            }
            $(window).resize(function () {
                q(function () {
                    if (!$.isXs()) {
                        var a = $(".am-scroller");
                        $(".nano-content", a).css({
                            "margin-right": 0
                        }), a.nanoScroller({
                            destroy: !0
                        }), a.nanoScroller()
                    }
                }, 500, "am_check_phone_classes")
            })
        }

        function c() {
            function a() {
                i.addClass(m.openRightSidebarClass + " " + m.transitionClass), o = !0
            }

            function b() {
                i.removeClass(m.openRightSidebarClass).addClass(m.transitionClass), d()
            }

            l.length > 0 && ($(".am-toggle-right-sidebar").on("click", function (c) {
                o && i.hasClass(m.openRightSidebarClass) ? b() : o || a(), c.stopPropagation(), c.preventDefault()
            }), $(document).on("mousedown touchstart", function (a) {
                !$(a.target).closest(l).length && i.hasClass(m.openRightSidebarClass) && b()
            }))
        }

        function d() {
            o = !0, setTimeout(function () {
                o = !1
            }, m.openSidebarDelay)
        }

        function e() {
            function a() {
                o || j.hasClass(m.removeLeftSidebarClass) || (i.addClass(m.openLeftSidebarClass + " " + m.transitionClass), o = !0)
            }

            function b() {
                !o && l.length > 0 && (i.addClass(m.openRightSidebarClass + " " + m.transitionClass), o = !0)
            }

            var c = !1;
            j.swipe({
                allowPageScroll: "vertical",
                preventDefaultEvents: !1,
                fallbackToMouseEvents: !1,
                swipeRight: function (c) {
                    p ? b() : a()
                },
                swipeLeft: function (c) {
                    p ? a() : b()
                },
                swipeStatus: function (a, b) {
                    switch (b) {
                        case "start":
                            o && (c = !0);
                            break;
                        case "end":
                            if (c) return c = !1, !1;
                            break;
                        case "cancel":
                            c = !1
                    }
                },
                threshold: m.swipeTreshold
            })
        }

        function f() {
            function a() {
                d.hasClass("chat-opened") || (d.addClass("chat-opened"), $(".am-scroller", h).nanoScroller())
            }

            function b() {
                d.hasClass("chat-opened") && d.removeClass("chat-opened")
            }

            function c(a, b) {
                var c = $('<li class="' + (b ? "self" : "friend") + '"></li>');
                "" != a && ($('<div class="msg">' + a + "</div>").appendTo(c), c.appendTo(i), j.stop().animate({
                    scrollTop: j.prop("scrollHeight")
                }, 900, "swing"), g())
            }

            var d = $(".am-right-sidebar .tab-pane.chat"),
                e = $(".chat-contacts", d),
                f = $(".chat-window", d),
                h = $(".chat-messages", f),
                i = $(".content > ul", h),
                j = $(".nano-content", h),
                k = $(".chat-input", f),
                l = $("input", k),
                m = $(".send-msg", k);
            $(".user a", e).on("click", function (b) {
                $(".am-scroller", e).nanoScroller({
                    stop: !0
                }), a(), b.preventDefault()
            }), $(".title .return", f).on("click", function (a) {
                b(), g()
            }), l.keypress(function (a) {
                var b = a.keyCode ? a.keyCode : a.which,
                    d = $(this).val();
                "13" == b && (c(d, !0), $(this).val("")), a.stopPropagation()
            }), m.on("click", function () {
                var a = l.val();
                c(a, !0), l.val("")
            })
        }

        function g() {
            $(".am-scroller").nanoScroller()
        }

        function h() {
            var a = 220,
                b = 500,
                c = $('<div class="am-scroll-top"></div>');
            c.appendTo("body"), $(window).on("scroll", function () {
                $(this).scrollTop() > a ? c.fadeIn(b) : c.fadeOut(b)
            }), c.on("touchstart mouseup", function (a) {
                $("html, body").animate({
                    scrollTop: 0
                }, b), a.preventDefault()
            })
        }

        var i, j, k, l, m = {
                assetsPath: "assets",
                imgPath: "img",
                jsPath: "js",
                libsPath: "lib",
                leftSidebarSlideSpeed: 200,
                enableSwipe: !0,
                swipeTreshold: 100,
                scrollTop: !0,
                openLeftSidebarClass: "open-left-sidebar",
                openRightSidebarClass: "open-right-sidebar",
                removeLeftSidebarClass: "am-nosidebar-left",
                openLeftSidebarOnClick: !1,
                openLeftSidebarOnClickClass: "am-left-sidebar--click",
                transitionClass: "am-animate",
                openSidebarDelay: 400,
                syncSubMenuOnHover: !1
            },
            n = {},
            o = !1,
            p = $("html").hasClass("rtl"),
            q = function () {
                var a = {};
                return function (b, c, d) {
                    d || (d = "x1x2x3x4"), a[d] && clearTimeout(a[d]), a[d] = setTimeout(b, c)
                }
            }();
        return {
            conf: m,
            color: n,
            init: function (d) {
                j = $(".am-wrapper"), k = $(".am-left-sidebar"), l = $(".am-right-sidebar"), i = $("body"), $.extend(m, d), b(), FastClick.attach(document.body), c(), f(), m.enableSwipe && e(), k.on("transitionend webkitTransitionEnd oTransitionEnd MSTransitionEnd", function () {
                    i.removeClass(m.transitionClass)
                }), m.scrollTop && h(), n.primary = a("clr-primary"), n.success = a("clr-success"), n.info = a("clr-info"), n.warning = a("clr-warning"), n.danger = a("clr-danger"), n.alt1 = a("clr-alt1"), n.alt2 = a("clr-alt2"), n.alt3 = a("clr-alt3"), n.alt4 = a("clr-alt4"), $(".am-connections").on("click", function (a) {
                    a.stopPropagation()
                }), g(), $(".dropdown").on("shown.bs.dropdown", function () {
                    $(".am-scroller").nanoScroller()
                }), $(".nav-tabs").on("shown.bs.tab", function (a) {
                    $(".am-scroller").nanoScroller()
                }), $('[data-toggle="tooltip"]').tooltip(), $('[data-toggle="popover"]').popover()
            },
            leftSidebarInit: b,
            rightSidebarInit: c,
            closeSubMenu: function () {
                var a = $(".sidebar-elements > li > ul:visible", k);
                a.addClass("hide"), setTimeout(function () {
                    a.removeClass("hide")
                }, 50)
            }
        }
    }(),
    deviceIsAndroid = navigator.userAgent.indexOf("Android") > 0,
    deviceIsIOS = /iP(ad|hone|od)/.test(navigator.userAgent),
    deviceIsIOS4 = deviceIsIOS && /OS 4_\d(_\d)?/.test(navigator.userAgent),
    deviceIsIOSWithBadTarget = deviceIsIOS && /OS ([6-9]|\d{2})_\d/.test(navigator.userAgent),
    deviceIsBlackBerry10 = navigator.userAgent.indexOf("BB10") > 0;
FastClick.prototype.needsClick = function (a) {
    "use strict";
    switch (a.nodeName.toLowerCase()) {
        case "button":
        case "select":
        case "textarea":
            if (a.disabled) return !0;
            break;
        case "input":
            if (deviceIsIOS && "file" === a.type || a.disabled) return !0;
            break;
        case "label":
        case "video":
            return !0
    }
    return /\bneedsclick\b/.test(a.className)
}, FastClick.prototype.needsFocus = function (a) {
    "use strict";
    switch (a.nodeName.toLowerCase()) {
        case "textarea":
            return !0;
        case "select":
            return !deviceIsAndroid;
        case "input":
            switch (a.type) {
                case "button":
                case "checkbox":
                case "file":
                case "image":
                case "radio":
                case "submit":
                    return !1
            }
            return !a.disabled && !a.readOnly;
        default:
            return /\bneedsfocus\b/.test(a.className)
    }
}, FastClick.prototype.sendClick = function (a, b) {
    "use strict";
    var c, d, e, f;
    document.activeElement && document.activeElement !== a && document.activeElement.blur(), f = b.changedTouches[0], e = document.createEvent("MouseEvents"), e.initMouseEvent("mousedown", !0, !0, window, 1, f.screenX, f.screenY, f.clientX, f.clientY, !1, !1, !1, !1, 0, null), e.forwardedTouchEvent = !0, a.dispatchEvent(e), d = document.createEvent("MouseEvents"), d.initMouseEvent("mouseup", !0, !0, window, 1, f.screenX, f.screenY, f.clientX, f.clientY, !1, !1, !1, !1, 0, null), d.forwardedTouchEvent = !0, a.dispatchEvent(d), c = document.createEvent("MouseEvents"), c.initMouseEvent(this.determineEventType(a), !0, !0, window, 1, f.screenX, f.screenY, f.clientX, f.clientY, !1, !1, !1, !1, 0, null), c.forwardedTouchEvent = !0, a.dispatchEvent(c)
}, FastClick.prototype.determineEventType = function (a) {
    "use strict";
    return deviceIsAndroid && "select" === a.tagName.toLowerCase() ? "mousedown" : "click"
}, FastClick.prototype.focus = function (a) {
    "use strict";
    var b;
    deviceIsIOS && a.setSelectionRange && 0 !== a.type.indexOf("date") && "time" !== a.type ? (b = a.value.length, a.setSelectionRange(b, b)) : a.focus()
}, FastClick.prototype.updateScrollParent = function (a) {
    "use strict";
    var b, c;
    if (b = a.fastClickScrollParent, !b || !b.contains(a)) {
        c = a;
        do {
            if (c.scrollHeight > c.offsetHeight) {
                b = c, a.fastClickScrollParent = c;
                break
            }
            c = c.parentElement
        } while (c)
    }
    b && (b.fastClickLastScrollTop = b.scrollTop)
}, FastClick.prototype.getTargetElementFromEventTarget = function (a) {
    "use strict";
    return a.nodeType === Node.TEXT_NODE ? a.parentNode : a
}, FastClick.prototype.onTouchStart = function (a) {
    "use strict";
    var b, c, d;
    if (a.targetTouches.length > 1) return !0;
    if (b = this.getTargetElementFromEventTarget(a.target), c = a.targetTouches[0], deviceIsIOS) {
        if (d = window.getSelection(), d.rangeCount && !d.isCollapsed) return !0;
        if (!deviceIsIOS4) {
            if (c.identifier && c.identifier === this.lastTouchIdentifier) return a.preventDefault(), !1;
            this.lastTouchIdentifier = c.identifier, this.updateScrollParent(b)
        }
    }
    return this.trackingClick = !0, this.trackingClickStart = a.timeStamp, this.targetElement = b, this.touchStartX = c.pageX, this.touchStartY = c.pageY, a.timeStamp - this.lastClickTime < this.tapDelay && a.preventDefault(), !0
}, FastClick.prototype.touchHasMoved = function (a) {
    "use strict";
    var b = a.changedTouches[0],
        c = this.touchBoundary;
    return Math.abs(b.pageX - this.touchStartX) > c || Math.abs(b.pageY - this.touchStartY) > c
}, FastClick.prototype.onTouchMove = function (a) {
    "use strict";
    return !this.trackingClick || ((this.targetElement !== this.getTargetElementFromEventTarget(a.target) || this.touchHasMoved(a)) && (this.trackingClick = !1, this.targetElement = null), !0)
}, FastClick.prototype.findControl = function (a) {
    "use strict";
    return void 0 !== a.control ? a.control : a.htmlFor ? document.getElementById(a.htmlFor) : a.querySelector("button, input:not([type=hidden]), keygen, meter, output, progress, select, textarea")
}, FastClick.prototype.onTouchEnd = function (a) {
    "use strict";
    var b, c, d, e, f, g = this.targetElement;
    if (!this.trackingClick) return !0;
    if (a.timeStamp - this.lastClickTime < this.tapDelay) return this.cancelNextClick = !0, !0;
    if (this.cancelNextClick = !1, this.lastClickTime = a.timeStamp, c = this.trackingClickStart, this.trackingClick = !1, this.trackingClickStart = 0, deviceIsIOSWithBadTarget && (f = a.changedTouches[0], g = document.elementFromPoint(f.pageX - window.pageXOffset, f.pageY - window.pageYOffset) || g, g.fastClickScrollParent = this.targetElement.fastClickScrollParent), d = g.tagName.toLowerCase(), "label" === d) {
        if (b = this.findControl(g)) {
            if (this.focus(g), deviceIsAndroid) return !1;
            g = b
        }
    } else if (this.needsFocus(g)) return a.timeStamp - c > 100 || deviceIsIOS && window.top !== window && "input" === d ? (this.targetElement = null, !1) : (this.focus(g), this.sendClick(g, a), deviceIsIOS && "select" === d || (this.targetElement = null, a.preventDefault()), !1);
    return !(!deviceIsIOS || deviceIsIOS4 || (e = g.fastClickScrollParent, !e || e.fastClickLastScrollTop === e.scrollTop)) || (this.needsClick(g) || (a.preventDefault(), this.sendClick(g, a)), !1)
}, FastClick.prototype.onTouchCancel = function () {
    "use strict";
    this.trackingClick = !1, this.targetElement = null
}, FastClick.prototype.onMouse = function (a) {
    "use strict";
    return !(this.targetElement && !a.forwardedTouchEvent && a.cancelable && (!this.needsClick(this.targetElement) || this.cancelNextClick) && (a.stopImmediatePropagation ? a.stopImmediatePropagation() : a.propagationStopped = !0, a.stopPropagation(), a.preventDefault(), 1))
}, FastClick.prototype.onClick = function (a) {
    "use strict";
    var b;
    return this.trackingClick ? (this.targetElement = null, this.trackingClick = !1, !0) : "submit" === a.target.type && 0 === a.detail || (b = this.onMouse(a), b || (this.targetElement = null), b)
}, FastClick.prototype.destroy = function () {
    "use strict";
    var a = this.layer;
    deviceIsAndroid && (a.removeEventListener("mouseover", this.onMouse, !0), a.removeEventListener("mousedown", this.onMouse, !0), a.removeEventListener("mouseup", this.onMouse, !0)), a.removeEventListener("click", this.onClick, !0), a.removeEventListener("touchstart", this.onTouchStart, !1), a.removeEventListener("touchmove", this.onTouchMove, !1), a.removeEventListener("touchend", this.onTouchEnd, !1), a.removeEventListener("touchcancel", this.onTouchCancel, !1)
}, FastClick.notNeeded = function (a) {
    "use strict";
    var b, c, d;
    if ("undefined" == typeof window.ontouchstart) return !0;
    if (c = +(/Chrome\/([0-9]+)/.exec(navigator.userAgent) || [, 0])[1]) {
        if (!deviceIsAndroid) return !0;
        if (b = document.querySelector("meta[name=viewport]")) {
            if (b.content.indexOf("user-scalable=no") !== -1) return !0;
            if (c > 31 && document.documentElement.scrollWidth <= window.outerWidth) return !0
        }
    }
    if (deviceIsBlackBerry10 && (d = navigator.userAgent.match(/Version\/([0-9]*)\.([0-9]*)/), d[1] >= 10 && d[2] >= 3 && (b = document.querySelector("meta[name=viewport]")))) {
        if (b.content.indexOf("user-scalable=no") !== -1) return !0;
        if (document.documentElement.scrollWidth <= window.outerWidth) return !0
    }
    return "none" === a.style.msTouchAction
}, FastClick.attach = function (a, b) {
    "use strict";
    return new FastClick(a, b)
}, "function" == typeof define && "object" == typeof define.amd && define.amd ? define(function () {
    "use strict";
    return FastClick
}) : "undefined" != typeof module && module.exports ? (module.exports = FastClick.attach, module.exports.FastClick = FastClick) : window.FastClick = FastClick, !function () {
    function a(c, d) {
        if (c = c ? c : "", d = d || {}, c instanceof a) return c;
        if (!(this instanceof a)) return new a(c, d);
        var e = b(c);
        this._originalInput = c, this._r = e.r, this._g = e.g, this._b = e.b, this._a = e.a, this._roundA = N(100 * this._a) / 100, this._format = d.format || e.format, this._gradientType = d.gradientType, this._r < 1 && (this._r = N(this._r)), this._g < 1 && (this._g = N(this._g)), this._b < 1 && (this._b = N(this._b)), this._ok = e.ok, this._tc_id = L++
    }

    function b(a) {
        var b = {
                r: 0,
                g: 0,
                b: 0
            },
            d = 1,
            f = !1,
            h = !1;
        return "string" == typeof a && (a = H(a)), "object" == typeof a && (a.hasOwnProperty("r") && a.hasOwnProperty("g") && a.hasOwnProperty("b") ? (b = c(a.r, a.g, a.b), f = !0, h = "%" === String(a.r).substr(-1) ? "prgb" : "rgb") : a.hasOwnProperty("h") && a.hasOwnProperty("s") && a.hasOwnProperty("v") ? (a.s = E(a.s), a.v = E(a.v), b = g(a.h, a.s, a.v), f = !0, h = "hsv") : a.hasOwnProperty("h") && a.hasOwnProperty("s") && a.hasOwnProperty("l") && (a.s = E(a.s), a.l = E(a.l), b = e(a.h, a.s, a.l), f = !0, h = "hsl"), a.hasOwnProperty("a") && (d = a.a)), d = x(d), {
            ok: f,
            format: a.format || h,
            r: O(255, P(b.r, 0)),
            g: O(255, P(b.g, 0)),
            b: O(255, P(b.b, 0)),
            a: d
        }
    }

    function c(a, b, c) {
        return {
            r: 255 * y(a, 255),
            g: 255 * y(b, 255),
            b: 255 * y(c, 255)
        }
    }

    function d(a, b, c) {
        a = y(a, 255), b = y(b, 255), c = y(c, 255);
        var d, e, f = P(a, b, c),
            g = O(a, b, c),
            h = (f + g) / 2;
        if (f == g) d = e = 0;
        else {
            var i = f - g;
            switch (e = h > .5 ? i / (2 - f - g) : i / (f + g), f) {
                case a:
                    d = (b - c) / i + (c > b ? 6 : 0);
                    break;
                case b:
                    d = (c - a) / i + 2;
                    break;
                case c:
                    d = (a - b) / i + 4
            }
            d /= 6
        }
        return {
            h: d,
            s: e,
            l: h
        }
    }

    function e(a, b, c) {
        function d(a, b, c) {
            return 0 > c && (c += 1), c > 1 && (c -= 1), 1 / 6 > c ? a + 6 * (b - a) * c : .5 > c ? b : 2 / 3 > c ? a + 6 * (b - a) * (2 / 3 - c) : a
        }

        var e, f, g;
        if (a = y(a, 360), b = y(b, 100), c = y(c, 100), 0 === b) e = f = g = c;
        else {
            var h = .5 > c ? c * (1 + b) : c + b - c * b,
                i = 2 * c - h;
            e = d(i, h, a + 1 / 3), f = d(i, h, a), g = d(i, h, a - 1 / 3)
        }
        return {
            r: 255 * e,
            g: 255 * f,
            b: 255 * g
        }
    }

    function f(a, b, c) {
        a = y(a, 255), b = y(b, 255), c = y(c, 255);
        var d, e, f = P(a, b, c),
            g = O(a, b, c),
            h = f,
            i = f - g;
        if (e = 0 === f ? 0 : i / f, f == g) d = 0;
        else {
            switch (f) {
                case a:
                    d = (b - c) / i + (c > b ? 6 : 0);
                    break;
                case b:
                    d = (c - a) / i + 2;
                    break;
                case c:
                    d = (a - b) / i + 4
            }
            d /= 6
        }
        return {
            h: d,
            s: e,
            v: h
        }
    }

    function g(a, b, c) {
        a = 6 * y(a, 360), b = y(b, 100), c = y(c, 100);
        var d = M.floor(a),
            e = a - d,
            f = c * (1 - b),
            g = c * (1 - e * b),
            h = c * (1 - (1 - e) * b),
            i = d % 6,
            j = [c, g, f, f, h, c][i],
            k = [h, c, c, g, f, f][i],
            l = [f, f, h, c, c, g][i];
        return {
            r: 255 * j,
            g: 255 * k,
            b: 255 * l
        }
    }

    function h(a, b, c, d) {
        var e = [D(N(a).toString(16)), D(N(b).toString(16)), D(N(c).toString(16))];
        return d && e[0].charAt(0) == e[0].charAt(1) && e[1].charAt(0) == e[1].charAt(1) && e[2].charAt(0) == e[2].charAt(1) ? e[0].charAt(0) + e[1].charAt(0) + e[2].charAt(0) : e.join("")
    }

    function i(a, b, c, d) {
        var e = [D(F(d)), D(N(a).toString(16)), D(N(b).toString(16)), D(N(c).toString(16))];
        return e.join("")
    }

    function j(b, c) {
        c = 0 === c ? 0 : c || 10;
        var d = a(b).toHsl();
        return d.s -= c / 100, d.s = z(d.s), a(d)
    }

    function k(b, c) {
        c = 0 === c ? 0 : c || 10;
        var d = a(b).toHsl();
        return d.s += c / 100, d.s = z(d.s), a(d)
    }

    function l(b) {
        return a(b).desaturate(100)
    }

    function m(b, c) {
        c = 0 === c ? 0 : c || 10;
        var d = a(b).toHsl();
        return d.l += c / 100, d.l = z(d.l), a(d)
    }

    function n(b, c) {
        c = 0 === c ? 0 : c || 10;
        var d = a(b).toRgb();
        return d.r = P(0, O(255, d.r - N(255 * -(c / 100)))), d.g = P(0, O(255, d.g - N(255 * -(c / 100)))), d.b = P(0, O(255, d.b - N(255 * -(c / 100)))), a(d)
    }

    function o(b, c) {
        c = 0 === c ? 0 : c || 10;
        var d = a(b).toHsl();
        return d.l -= c / 100, d.l = z(d.l), a(d)
    }

    function p(b, c) {
        var d = a(b).toHsl(),
            e = (N(d.h) + c) % 360;
        return d.h = 0 > e ? 360 + e : e, a(d)
    }

    function q(b) {
        var c = a(b).toHsl();
        return c.h = (c.h + 180) % 360, a(c)
    }

    function r(b) {
        var c = a(b).toHsl(),
            d = c.h;
        return [a(b), a({
            h: (d + 120) % 360,
            s: c.s,
            l: c.l
        }), a({
            h: (d + 240) % 360,
            s: c.s,
            l: c.l
        })]
    }

    function s(b) {
        var c = a(b).toHsl(),
            d = c.h;
        return [a(b), a({
            h: (d + 90) % 360,
            s: c.s,
            l: c.l
        }), a({
            h: (d + 180) % 360,
            s: c.s,
            l: c.l
        }), a({
            h: (d + 270) % 360,
            s: c.s,
            l: c.l
        })]
    }

    function t(b) {
        var c = a(b).toHsl(),
            d = c.h;
        return [a(b), a({
            h: (d + 72) % 360,
            s: c.s,
            l: c.l
        }), a({
            h: (d + 216) % 360,
            s: c.s,
            l: c.l
        })]
    }

    function u(b, c, d) {
        c = c || 6, d = d || 30;
        var e = a(b).toHsl(),
            f = 360 / d,
            g = [a(b)];
        for (e.h = (e.h - (f * c >> 1) + 720) % 360; --c;) e.h = (e.h + f) % 360, g.push(a(e));
        return g
    }

    function v(b, c) {
        c = c || 6;
        for (var d = a(b).toHsv(), e = d.h, f = d.s, g = d.v, h = [], i = 1 / c; c--;) h.push(a({
            h: e,
            s: f,
            v: g
        })), g = (g + i) % 1;
        return h
    }

    function w(a) {
        var b = {};
        for (var c in a) a.hasOwnProperty(c) && (b[a[c]] = c);
        return b
    }

    function x(a) {
        return a = parseFloat(a), (isNaN(a) || 0 > a || a > 1) && (a = 1), a
    }

    function y(a, b) {
        B(a) && (a = "100%");
        var c = C(a);
        return a = O(b, P(0, parseFloat(a))), c && (a = parseInt(a * b, 10) / 100), M.abs(a - b) < 1e-6 ? 1 : a % b / parseFloat(b)
    }

    function z(a) {
        return O(1, P(0, a))
    }

    function A(a) {
        return parseInt(a, 16)
    }

    function B(a) {
        return "string" == typeof a && -1 != a.indexOf(".") && 1 === parseFloat(a)
    }

    function C(a) {
        return "string" == typeof a && -1 != a.indexOf("%")
    }

    function D(a) {
        return 1 == a.length ? "0" + a : "" + a
    }

    function E(a) {
        return 1 >= a && (a = 100 * a + "%"), a
    }

    function F(a) {
        return Math.round(255 * parseFloat(a)).toString(16)
    }

    function G(a) {
        return A(a) / 255
    }

    function H(a) {
        a = a.replace(J, "").replace(K, "").toLowerCase();
        var b = !1;
        if (R[a]) a = R[a], b = !0;
        else if ("transparent" == a) return {
            r: 0,
            g: 0,
            b: 0,
            a: 0,
            format: "name"
        };
        var c;
        return (c = T.rgb.exec(a)) ? {
            r: c[1],
            g: c[2],
            b: c[3]
        } : (c = T.rgba.exec(a)) ? {
            r: c[1],
            g: c[2],
            b: c[3],
            a: c[4]
        } : (c = T.hsl.exec(a)) ? {
            h: c[1],
            s: c[2],
            l: c[3]
        } : (c = T.hsla.exec(a)) ? {
            h: c[1],
            s: c[2],
            l: c[3],
            a: c[4]
        } : (c = T.hsv.exec(a)) ? {
            h: c[1],
            s: c[2],
            v: c[3]
        } : (c = T.hsva.exec(a)) ? {
            h: c[1],
            s: c[2],
            v: c[3],
            a: c[4]
        } : (c = T.hex8.exec(a)) ? {
            a: G(c[1]),
            r: A(c[2]),
            g: A(c[3]),
            b: A(c[4]),
            format: b ? "name" : "hex8"
        } : (c = T.hex6.exec(a)) ? {
            r: A(c[1]),
            g: A(c[2]),
            b: A(c[3]),
            format: b ? "name" : "hex"
        } : !!(c = T.hex3.exec(a)) && {
            r: A(c[1] + "" + c[1]),
            g: A(c[2] + "" + c[2]),
            b: A(c[3] + "" + c[3]),
            format: b ? "name" : "hex"
        }
    }

    function I(a) {
        var b, c;
        return a = a || {
            level: "AA",
            size: "small"
        }, b = (a.level || "AA").toUpperCase(), c = (a.size || "small").toLowerCase(), "AA" !== b && "AAA" !== b && (b = "AA"), "small" !== c && "large" !== c && (c = "small"), {
            level: b,
            size: c
        }
    }

    var J = /^[\s,#]+/,
        K = /\s+$/,
        L = 0,
        M = Math,
        N = M.round,
        O = M.min,
        P = M.max,
        Q = M.random;
    a.prototype = {
        isDark: function () {
            return this.getBrightness() < 128
        },
        isLight: function () {
            return !this.isDark()
        },
        isValid: function () {
            return this._ok
        },
        getOriginalInput: function () {
            return this._originalInput
        },
        getFormat: function () {
            return this._format
        },
        getAlpha: function () {
            return this._a
        },
        getBrightness: function () {
            var a = this.toRgb();
            return (299 * a.r + 587 * a.g + 114 * a.b) / 1e3
        },
        getLuminance: function () {
            var a, b, c, d, e, f, g = this.toRgb();
            return a = g.r / 255, b = g.g / 255, c = g.b / 255, d = .03928 >= a ? a / 12.92 : Math.pow((a + .055) / 1.055, 2.4), e = .03928 >= b ? b / 12.92 : Math.pow((b + .055) / 1.055, 2.4), f = .03928 >= c ? c / 12.92 : Math.pow((c + .055) / 1.055, 2.4), .2126 * d + .7152 * e + .0722 * f
        },
        setAlpha: function (a) {
            return this._a = x(a), this._roundA = N(100 * this._a) / 100, this
        },
        toHsv: function () {
            var a = f(this._r, this._g, this._b);
            return {
                h: 360 * a.h,
                s: a.s,
                v: a.v,
                a: this._a
            }
        },
        toHsvString: function () {
            var a = f(this._r, this._g, this._b),
                b = N(360 * a.h),
                c = N(100 * a.s),
                d = N(100 * a.v);
            return 1 == this._a ? "hsv(" + b + ", " + c + "%, " + d + "%)" : "hsva(" + b + ", " + c + "%, " + d + "%, " + this._roundA + ")"
        },
        toHsl: function () {
            var a = d(this._r, this._g, this._b);
            return {
                h: 360 * a.h,
                s: a.s,
                l: a.l,
                a: this._a
            }
        },
        toHslString: function () {
            var a = d(this._r, this._g, this._b),
                b = N(360 * a.h),
                c = N(100 * a.s),
                e = N(100 * a.l);
            return 1 == this._a ? "hsl(" + b + ", " + c + "%, " + e + "%)" : "hsla(" + b + ", " + c + "%, " + e + "%, " + this._roundA + ")"
        },
        toHex: function (a) {
            return h(this._r, this._g, this._b, a)
        },
        toHexString: function (a) {
            return "#" + this.toHex(a)
        },
        toHex8: function () {
            return i(this._r, this._g, this._b, this._a)
        },
        toHex8String: function () {
            return "#" + this.toHex8()
        },
        toRgb: function () {
            return {
                r: N(this._r),
                g: N(this._g),
                b: N(this._b),
                a: this._a
            }
        },
        toRgbString: function () {
            return 1 == this._a ? "rgb(" + N(this._r) + ", " + N(this._g) + ", " + N(this._b) + ")" : "rgba(" + N(this._r) + ", " + N(this._g) + ", " + N(this._b) + ", " + this._roundA + ")"
        },
        toPercentageRgb: function () {
            return {
                r: N(100 * y(this._r, 255)) + "%",
                g: N(100 * y(this._g, 255)) + "%",
                b: N(100 * y(this._b, 255)) + "%",
                a: this._a
            }
        },
        toPercentageRgbString: function () {
            return 1 == this._a ? "rgb(" + N(100 * y(this._r, 255)) + "%, " + N(100 * y(this._g, 255)) + "%, " + N(100 * y(this._b, 255)) + "%)" : "rgba(" + N(100 * y(this._r, 255)) + "%, " + N(100 * y(this._g, 255)) + "%, " + N(100 * y(this._b, 255)) + "%, " + this._roundA + ")"
        },
        toName: function () {
            return 0 === this._a ? "transparent" : !(this._a < 1) && (S[h(this._r, this._g, this._b, !0)] || !1)
        },
        toFilter: function (b) {
            var c = "#" + i(this._r, this._g, this._b, this._a),
                d = c,
                e = this._gradientType ? "GradientType = 1, " : "";
            if (b) {
                var f = a(b);
                d = f.toHex8String()
            }
            return "progid:DXImageTransform.Microsoft.gradient(" + e + "startColorstr=" + c + ",endColorstr=" + d + ")"
        },
        toString: function (a) {
            var b = !!a;
            a = a || this._format;
            var c = !1,
                d = this._a < 1 && this._a >= 0,
                e = !b && d && ("hex" === a || "hex6" === a || "hex3" === a || "name" === a);
            return e ? "name" === a && 0 === this._a ? this.toName() : this.toRgbString() : ("rgb" === a && (c = this.toRgbString()), "prgb" === a && (c = this.toPercentageRgbString()), ("hex" === a || "hex6" === a) && (c = this.toHexString()), "hex3" === a && (c = this.toHexString(!0)), "hex8" === a && (c = this.toHex8String()), "name" === a && (c = this.toName()), "hsl" === a && (c = this.toHslString()), "hsv" === a && (c = this.toHsvString()), c || this.toHexString())
        },
        _applyModification: function (a, b) {
            var c = a.apply(null, [this].concat([].slice.call(b)));
            return this._r = c._r, this._g = c._g, this._b = c._b, this.setAlpha(c._a), this
        },
        lighten: function () {
            return this._applyModification(m, arguments)
        },
        brighten: function () {
            return this._applyModification(n, arguments)
        },
        darken: function () {
            return this._applyModification(o, arguments)
        },
        desaturate: function () {
            return this._applyModification(j, arguments)
        },
        saturate: function () {
            return this._applyModification(k, arguments)
        },
        greyscale: function () {
            return this._applyModification(l, arguments)
        },
        spin: function () {
            return this._applyModification(p, arguments)
        },
        _applyCombination: function (a, b) {
            return a.apply(null, [this].concat([].slice.call(b)))
        },
        analogous: function () {
            return this._applyCombination(u, arguments)
        },
        complement: function () {
            return this._applyCombination(q, arguments)
        },
        monochromatic: function () {
            return this._applyCombination(v, arguments)
        },
        splitcomplement: function () {
            return this._applyCombination(t, arguments)
        },
        triad: function () {
            return this._applyCombination(r, arguments)
        },
        tetrad: function () {
            return this._applyCombination(s, arguments)
        }
    }, a.fromRatio = function (b, c) {
        if ("object" == typeof b) {
            var d = {};
            for (var e in b) b.hasOwnProperty(e) && (d[e] = "a" === e ? b[e] : E(b[e]));
            b = d
        }
        return a(b, c)
    }, a.equals = function (b, c) {
        return !(!b || !c) && a(b).toRgbString() == a(c).toRgbString()
    }, a.random = function () {
        return a.fromRatio({
            r: Q(),
            g: Q(),
            b: Q()
        })
    }, a.mix = function (b, c, d) {
        d = 0 === d ? 0 : d || 50;
        var e, f = a(b).toRgb(),
            g = a(c).toRgb(),
            h = d / 100,
            i = 2 * h - 1,
            j = g.a - f.a;
        e = -1 == i * j ? i : (i + j) / (1 + i * j), e = (e + 1) / 2;
        var k = 1 - e,
            l = {
                r: g.r * e + f.r * k,
                g: g.g * e + f.g * k,
                b: g.b * e + f.b * k,
                a: g.a * h + f.a * (1 - h)
            };
        return a(l)
    }, a.readability = function (b, c) {
        var d = a(b),
            e = a(c);
        return (Math.max(d.getLuminance(), e.getLuminance()) + .05) / (Math.min(d.getLuminance(), e.getLuminance()) + .05)
    }, a.isReadable = function (b, c, d) {
        var e, f, g = a.readability(b, c);
        switch (f = !1, e = I(d), e.level + e.size) {
            case "AAsmall":
            case "AAAlarge":
                f = g >= 4.5;
                break;
            case "AAlarge":
                f = g >= 3;
                break;
            case "AAAsmall":
                f = g >= 7
        }
        return f
    }, a.mostReadable = function (b, c, d) {
        var e, f, g, h, i = null,
            j = 0;
        d = d || {}, f = d.includeFallbackColors, g = d.level, h = d.size;
        for (var k = 0; k < c.length; k++) e = a.readability(b, c[k]), e > j && (j = e, i = a(c[k]));
        return a.isReadable(b, i, {
            level: g,
            size: h
        }) || !f ? i : (d.includeFallbackColors = !1, a.mostReadable(b, ["#fff", "#000"], d))
    };
    var R = a.names = {
            aliceblue: "f0f8ff",
            antiquewhite: "faebd7",
            aqua: "0ff",
            aquamarine: "7fffd4",
            azure: "f0ffff",
            beige: "f5f5dc",
            bisque: "ffe4c4",
            black: "000",
            blanchedalmond: "ffebcd",
            blue: "00f",
            blueviolet: "8a2be2",
            brown: "a52a2a",
            burlywood: "deb887",
            burntsienna: "ea7e5d",
            cadetblue: "5f9ea0",
            chartreuse: "7fff00",
            chocolate: "d2691e",
            coral: "ff7f50",
            cornflowerblue: "6495ed",
            cornsilk: "fff8dc",
            crimson: "dc143c",
            cyan: "0ff",
            darkblue: "00008b",
            darkcyan: "008b8b",
            darkgoldenrod: "b8860b",
            darkgray: "a9a9a9",
            darkgreen: "006400",
            darkgrey: "a9a9a9",
            darkkhaki: "bdb76b",
            darkmagenta: "8b008b",
            darkolivegreen: "556b2f",
            darkorange: "ff8c00",
            darkorchid: "9932cc",
            darkred: "8b0000",
            darksalmon: "e9967a",
            darkseagreen: "8fbc8f",
            darkslateblue: "483d8b",
            darkslategray: "2f4f4f",
            darkslategrey: "2f4f4f",
            darkturquoise: "00ced1",
            darkviolet: "9400d3",
            deeppink: "ff1493",
            deepskyblue: "00bfff",
            dimgray: "696969",
            dimgrey: "696969",
            dodgerblue: "1e90ff",
            firebrick: "b22222",
            floralwhite: "fffaf0",
            forestgreen: "228b22",
            fuchsia: "f0f",
            gainsboro: "dcdcdc",
            ghostwhite: "f8f8ff",
            gold: "ffd700",
            goldenrod: "daa520",
            gray: "808080",
            green: "008000",
            greenyellow: "adff2f",
            grey: "808080",
            honeydew: "f0fff0",
            hotpink: "ff69b4",
            indianred: "cd5c5c",
            indigo: "4b0082",
            ivory: "fffff0",
            khaki: "f0e68c",
            lavender: "e6e6fa",
            lavenderblush: "fff0f5",
            lawngreen: "7cfc00",
            lemonchiffon: "fffacd",
            lightblue: "add8e6",
            lightcoral: "f08080",
            lightcyan: "e0ffff",
            lightgoldenrodyellow: "fafad2",
            lightgray: "d3d3d3",
            lightgreen: "90ee90",
            lightgrey: "d3d3d3",
            lightpink: "ffb6c1",
            lightsalmon: "ffa07a",
            lightseagreen: "20b2aa",
            lightskyblue: "87cefa",
            lightslategray: "789",
            lightslategrey: "789",
            lightsteelblue: "b0c4de",
            lightyellow: "ffffe0",
            lime: "0f0",
            limegreen: "32cd32",
            linen: "faf0e6",
            magenta: "f0f",
            maroon: "800000",
            mediumaquamarine: "66cdaa",
            mediumblue: "0000cd",
            mediumorchid: "ba55d3",
            mediumpurple: "9370db",
            mediumseagreen: "3cb371",
            mediumslateblue: "7b68ee",
            mediumspringgreen: "00fa9a",
            mediumturquoise: "48d1cc",
            mediumvioletred: "c71585",
            midnightblue: "191970",
            mintcream: "f5fffa",
            mistyrose: "ffe4e1",
            moccasin: "ffe4b5",
            navajowhite: "ffdead",
            navy: "000080",
            oldlace: "fdf5e6",
            olive: "808000",
            olivedrab: "6b8e23",
            orange: "ffa500",
            orangered: "ff4500",
            orchid: "da70d6",
            palegoldenrod: "eee8aa",
            palegreen: "98fb98",
            paleturquoise: "afeeee",
            palevioletred: "db7093",
            papayawhip: "ffefd5",
            peachpuff: "ffdab9",
            peru: "cd853f",
            pink: "ffc0cb",
            plum: "dda0dd",
            powderblue: "b0e0e6",
            purple: "800080",
            rebeccapurple: "663399",
            red: "f00",
            rosybrown: "bc8f8f",
            royalblue: "4169e1",
            saddlebrown: "8b4513",
            salmon: "fa8072",
            sandybrown: "f4a460",
            seagreen: "2e8b57",
            seashell: "fff5ee",
            sienna: "a0522d",
            silver: "c0c0c0",
            skyblue: "87ceeb",
            slateblue: "6a5acd",
            slategray: "708090",
            slategrey: "708090",
            snow: "fffafa",
            springgreen: "00ff7f",
            steelblue: "4682b4",
            tan: "d2b48c",
            teal: "008080",
            thistle: "d8bfd8",
            tomato: "ff6347",
            turquoise: "40e0d0",
            violet: "ee82ee",
            wheat: "f5deb3",
            white: "fff",
            whitesmoke: "f5f5f5",
            yellow: "ff0",
            yellowgreen: "9acd32"
        },
        S = a.hexNames = w(R),
        T = function () {
            var a = "[-\\+]?\\d+%?",
                b = "[-\\+]?\\d*\\.\\d+%?",
                c = "(?:" + b + ")|(?:" + a + ")",
                d = "[\\s|\\(]+(" + c + ")[,|\\s]+(" + c + ")[,|\\s]+(" + c + ")\\s*\\)?",
                e = "[\\s|\\(]+(" + c + ")[,|\\s]+(" + c + ")[,|\\s]+(" + c + ")[,|\\s]+(" + c + ")\\s*\\)?";
            return {
                rgb: new RegExp("rgb" + d),
                rgba: new RegExp("rgba" + e),
                hsl: new RegExp("hsl" + d),
                hsla: new RegExp("hsla" + e),
                hsv: new RegExp("hsv" + d),
                hsva: new RegExp("hsva" + e),
                hex3: /^([0-9a-fA-F]{1})([0-9a-fA-F]{1})([0-9a-fA-F]{1})$/,
                hex6: /^([0-9a-fA-F]{2})([0-9a-fA-F]{2})([0-9a-fA-F]{2})$/,
                hex8: /^([0-9a-fA-F]{2})([0-9a-fA-F]{2})([0-9a-fA-F]{2})([0-9a-fA-F]{2})$/
            }
        }();
    "undefined" != typeof module && module.exports ? module.exports = a : "function" == typeof define && define.amd ? define(function () {
        return a
    }) : window.tinycolor = a
}(), !function (a) {
    "function" == typeof define && define.amd && define.amd.jQuery ? define(["jquery"], a) : a("undefined" != typeof module && module.exports ? require("jquery") : jQuery)
}(function (a) {
    "use strict";

    function b(b) {
        return !b || void 0 !== b.allowPageScroll || void 0 === b.swipe && void 0 === b.swipeStatus || (b.allowPageScroll = k), void 0 !== b.click && void 0 === b.tap && (b.tap = b.click), b || (b = {}), b = a.extend({}, a.fn.swipe.defaults, b), this.each(function () {
            var d = a(this),
                e = d.data(C);
            e || (e = new c(this, b), d.data(C, e))
        })
    }

    function c(b, c) {
        function d(b) {
            if (!(ja() || a(b.target).closest(c.excludedElements, Ta).length > 0)) {
                var d = b.originalEvent ? b.originalEvent : b;
                if (!d.pointerType || "mouse" != d.pointerType || 0 != c.fallbackToMouseEvents) {
                    var e, f = d.touches,
                        g = f ? f[0] : d;
                    return Ua = v, f ? Va = f.length : c.preventDefaultEvents !== !1 && b.preventDefault(), Ja = 0, Ka = null, La = null, Ra = null, Ma = 0, Na = 0, Oa = 0, Pa = 1, Qa = 0, Sa = qa(), ha(), la(0, g), !f || Va === c.fingers || c.fingers === t || R() ? (Xa = za(), 2 == Va && (la(1, f[1]), Na = Oa = ta(Wa[0].start, Wa[1].start)), (c.swipeStatus || c.pinchStatus) && (e = J(d, Ua))) : e = !1, e === !1 ? (Ua = y, J(d, Ua), e) : (c.hold && (bb = setTimeout(a.proxy(function () {
                        Ta.trigger("hold", [d.target]), c.hold && (e = c.hold.call(Ta, d, d.target))
                    }, this), c.longTapThreshold)), ka(!0), null)
                }
            }
        }

        function D(a) {
            var b = a.originalEvent ? a.originalEvent : a;
            if (Ua !== x && Ua !== y && !ia()) {
                var d, e = b.touches,
                    f = e ? e[0] : b,
                    g = ma(f);
                if (Ya = za(), e && (Va = e.length), c.hold && clearTimeout(bb), Ua = w, 2 == Va && (0 == Na ? (la(1, e[1]), Na = Oa = ta(Wa[0].start, Wa[1].start)) : (ma(e[1]), Oa = ta(Wa[0].end, Wa[1].end), Ra = va(Wa[0].end, Wa[1].end)), Pa = ua(Na, Oa), Qa = Math.abs(Na - Oa)), Va === c.fingers || c.fingers === t || !e || R()) {
                    if (Ka = ya(g.start, g.end), La = ya(g.last, g.end), P(a, La), Ja = wa(g.start, g.end), Ma = sa(), oa(Ka, Ja), d = J(b, Ua), !c.triggerOnTouchEnd || c.triggerOnTouchLeave) {
                        var h = !0;
                        if (c.triggerOnTouchLeave) {
                            var i = Aa(this);
                            h = Ba(g.end, i)
                        }
                        !c.triggerOnTouchEnd && h ? Ua = I(w) : c.triggerOnTouchLeave && !h && (Ua = I(x)), Ua != y && Ua != x || J(b, Ua)
                    }
                } else Ua = y, J(b, Ua);
                d === !1 && (Ua = y, J(b, Ua))
            }
        }

        function E(a) {
            var b = a.originalEvent ? a.originalEvent : a,
                d = b.touches;
            if (d) {
                if (d.length && !ia()) return ga(b), !0;
                if (d.length && ia()) return !0
            }
            return ia() && (Va = $a), Ya = za(), Ma = sa(), M() || !L() ? (Ua = y,
                J(b, Ua)) : c.triggerOnTouchEnd || c.triggerOnTouchEnd === !1 && Ua === w ? (c.preventDefaultEvents !== !1 && a.preventDefault(), Ua = x, J(b, Ua)) : !c.triggerOnTouchEnd && Y() ? (Ua = x, K(b, Ua, o)) : Ua === w && (Ua = y, J(b, Ua)), ka(!1), null
        }

        function F() {
            Va = 0, Ya = 0, Xa = 0, Na = 0, Oa = 0, Pa = 1, ha(), ka(!1)
        }

        function G(a) {
            var b = a.originalEvent ? a.originalEvent : a;
            c.triggerOnTouchLeave && (Ua = I(x), J(b, Ua))
        }

        function H() {
            Ta.unbind(Ea, d), Ta.unbind(Ia, F), Ta.unbind(Fa, D), Ta.unbind(Ga, E), Ha && Ta.unbind(Ha, G), ka(!1)
        }

        function I(a) {
            var b = a,
                d = O(),
                e = L(),
                f = M();
            return !d || f ? b = y : !e || a != w || c.triggerOnTouchEnd && !c.triggerOnTouchLeave ? !e && a == x && c.triggerOnTouchLeave && (b = y) : b = x, b
        }

        function J(a, b) {
            var c, d = a.touches;
            return (V() || U()) && (c = K(a, b, m)), (S() || R()) && c !== !1 && (c = K(a, b, n)), ea() && c !== !1 ? c = K(a, b, p) : fa() && c !== !1 ? c = K(a, b, q) : da() && c !== !1 && (c = K(a, b, o)), b === y && F(a), b === x && (d ? d.length || F(a) : F(a)), c
        }

        function K(b, d, k) {
            var l;
            if (k == m) {
                if (Ta.trigger("swipeStatus", [d, Ka || null, Ja || 0, Ma || 0, Va, Wa, La]), c.swipeStatus && (l = c.swipeStatus.call(Ta, b, d, Ka || null, Ja || 0, Ma || 0, Va, Wa, La), l === !1)) return !1;
                if (d == x && T()) {
                    if (clearTimeout(ab), clearTimeout(bb), Ta.trigger("swipe", [Ka, Ja, Ma, Va, Wa, La]), c.swipe && (l = c.swipe.call(Ta, b, Ka, Ja, Ma, Va, Wa, La), l === !1)) return !1;
                    switch (Ka) {
                        case e:
                            Ta.trigger("swipeLeft", [Ka, Ja, Ma, Va, Wa, La]), c.swipeLeft && (l = c.swipeLeft.call(Ta, b, Ka, Ja, Ma, Va, Wa, La));
                            break;
                        case f:
                            Ta.trigger("swipeRight", [Ka, Ja, Ma, Va, Wa, La]), c.swipeRight && (l = c.swipeRight.call(Ta, b, Ka, Ja, Ma, Va, Wa, La));
                            break;
                        case g:
                            Ta.trigger("swipeUp", [Ka, Ja, Ma, Va, Wa, La]), c.swipeUp && (l = c.swipeUp.call(Ta, b, Ka, Ja, Ma, Va, Wa, La));
                            break;
                        case h:
                            Ta.trigger("swipeDown", [Ka, Ja, Ma, Va, Wa, La]), c.swipeDown && (l = c.swipeDown.call(Ta, b, Ka, Ja, Ma, Va, Wa, La))
                    }
                }
            }
            if (k == n) {
                if (Ta.trigger("pinchStatus", [d, Ra || null, Qa || 0, Ma || 0, Va, Pa, Wa]), c.pinchStatus && (l = c.pinchStatus.call(Ta, b, d, Ra || null, Qa || 0, Ma || 0, Va, Pa, Wa), l === !1)) return !1;
                if (d == x && Q()) switch (Ra) {
                    case i:
                        Ta.trigger("pinchIn", [Ra || null, Qa || 0, Ma || 0, Va, Pa, Wa]), c.pinchIn && (l = c.pinchIn.call(Ta, b, Ra || null, Qa || 0, Ma || 0, Va, Pa, Wa));
                        break;
                    case j:
                        Ta.trigger("pinchOut", [Ra || null, Qa || 0, Ma || 0, Va, Pa, Wa]), c.pinchOut && (l = c.pinchOut.call(Ta, b, Ra || null, Qa || 0, Ma || 0, Va, Pa, Wa))
                }
            }
            return k == o ? d !== y && d !== x || (clearTimeout(ab), clearTimeout(bb), Z() && !aa() ? (_a = za(), ab = setTimeout(a.proxy(function () {
                _a = null, Ta.trigger("tap", [b.target]), c.tap && (l = c.tap.call(Ta, b, b.target))
            }, this), c.doubleTapThreshold)) : (_a = null, Ta.trigger("tap", [b.target]), c.tap && (l = c.tap.call(Ta, b, b.target)))) : k == p ? d !== y && d !== x || (clearTimeout(ab), clearTimeout(bb), _a = null, Ta.trigger("doubletap", [b.target]), c.doubleTap && (l = c.doubleTap.call(Ta, b, b.target))) : k == q && (d !== y && d !== x || (clearTimeout(ab), _a = null, Ta.trigger("longtap", [b.target]), c.longTap && (l = c.longTap.call(Ta, b, b.target)))), l
        }

        function L() {
            var a = !0;
            return null !== c.threshold && (a = Ja >= c.threshold), a
        }

        function M() {
            var a = !1;
            return null !== c.cancelThreshold && null !== Ka && (a = pa(Ka) - Ja >= c.cancelThreshold), a
        }

        function N() {
            return null === c.pinchThreshold || Qa >= c.pinchThreshold
        }

        function O() {
            var a;
            return a = !c.maxTimeThreshold || !(Ma >= c.maxTimeThreshold)
        }

        function P(a, b) {
            if (c.preventDefaultEvents !== !1)
                if (c.allowPageScroll === k) a.preventDefault();
                else {
                    var d = c.allowPageScroll === l;
                    switch (b) {
                        case e:
                            (c.swipeLeft && d || !d && c.allowPageScroll != r) && a.preventDefault();
                            break;
                        case f:
                            (c.swipeRight && d || !d && c.allowPageScroll != r) && a.preventDefault();
                            break;
                        case g:
                            (c.swipeUp && d || !d && c.allowPageScroll != s) && a.preventDefault();
                            break;
                        case h:
                            (c.swipeDown && d || !d && c.allowPageScroll != s) && a.preventDefault();
                            break;
                        case k:
                    }
                }
        }

        function Q() {
            var a = W(),
                b = X(),
                c = N();
            return a && b && c
        }

        function R() {
            return !!(c.pinchStatus || c.pinchIn || c.pinchOut)
        }

        function S() {
            return !(!Q() || !R())
        }

        function T() {
            var a = O(),
                b = L(),
                c = W(),
                d = X(),
                e = M(),
                f = !e && d && c && b && a;
            return f
        }

        function U() {
            return !!(c.swipe || c.swipeStatus || c.swipeLeft || c.swipeRight || c.swipeUp || c.swipeDown)
        }

        function V() {
            return !(!T() || !U())
        }

        function W() {
            return Va === c.fingers || c.fingers === t || !z
        }

        function X() {
            return 0 !== Wa[0].end.x
        }

        function Y() {
            return !!c.tap
        }

        function Z() {
            return !!c.doubleTap
        }

        function $() {
            return !!c.longTap
        }

        function _() {
            if (null == _a) return !1;
            var a = za();
            return Z() && a - _a <= c.doubleTapThreshold
        }

        function aa() {
            return _()
        }

        function ba() {
            return (1 === Va || !z) && (isNaN(Ja) || Ja < c.threshold)
        }

        function ca() {
            return Ma > c.longTapThreshold && u > Ja
        }

        function da() {
            return !(!ba() || !Y())
        }

        function ea() {
            return !(!_() || !Z())
        }

        function fa() {
            return !(!ca() || !$())
        }

        function ga(a) {
            Za = za(), $a = a.touches.length + 1
        }

        function ha() {
            Za = 0, $a = 0
        }

        function ia() {
            var a = !1;
            if (Za) {
                var b = za() - Za;
                b <= c.fingerReleaseThreshold && (a = !0)
            }
            return a
        }

        function ja() {
            return !(Ta.data(C + "_intouch") !== !0)
        }

        function ka(a) {
            Ta && (a === !0 ? (Ta.bind(Fa, D), Ta.bind(Ga, E), Ha && Ta.bind(Ha, G)) : (Ta.unbind(Fa, D, !1), Ta.unbind(Ga, E, !1), Ha && Ta.unbind(Ha, G, !1)), Ta.data(C + "_intouch", a === !0))
        }

        function la(a, b) {
            var c = {
                start: {
                    x: 0,
                    y: 0
                },
                last: {
                    x: 0,
                    y: 0
                },
                end: {
                    x: 0,
                    y: 0
                }
            };
            return c.start.x = c.last.x = c.end.x = b.pageX || b.clientX, c.start.y = c.last.y = c.end.y = b.pageY || b.clientY, Wa[a] = c, c
        }

        function ma(a) {
            var b = void 0 !== a.identifier ? a.identifier : 0,
                c = na(b);
            return null === c && (c = la(b, a)), c.last.x = c.end.x, c.last.y = c.end.y, c.end.x = a.pageX || a.clientX, c.end.y = a.pageY || a.clientY, c
        }

        function na(a) {
            return Wa[a] || null
        }

        function oa(a, b) {
            a != k && (b = Math.max(b, pa(a)), Sa[a].distance = b)
        }

        function pa(a) {
            return Sa[a] ? Sa[a].distance : void 0
        }

        function qa() {
            var a = {};
            return a[e] = ra(e), a[f] = ra(f), a[g] = ra(g), a[h] = ra(h), a
        }

        function ra(a) {
            return {
                direction: a,
                distance: 0
            }
        }

        function sa() {
            return Ya - Xa
        }

        function ta(a, b) {
            var c = Math.abs(a.x - b.x),
                d = Math.abs(a.y - b.y);
            return Math.round(Math.sqrt(c * c + d * d))
        }

        function ua(a, b) {
            var c = b / a * 1;
            return c.toFixed(2)
        }

        function va() {
            return 1 > Pa ? j : i
        }

        function wa(a, b) {
            return Math.round(Math.sqrt(Math.pow(b.x - a.x, 2) + Math.pow(b.y - a.y, 2)))
        }

        function xa(a, b) {
            var c = a.x - b.x,
                d = b.y - a.y,
                e = Math.atan2(d, c),
                f = Math.round(180 * e / Math.PI);
            return 0 > f && (f = 360 - Math.abs(f)), f
        }

        function ya(a, b) {
            if (Ca(a, b)) return k;
            var c = xa(a, b);
            return 45 >= c && c >= 0 ? e : 360 >= c && c >= 315 ? e : c >= 135 && 225 >= c ? f : c > 45 && 135 > c ? h : g
        }

        function za() {
            var a = new Date;
            return a.getTime()
        }

        function Aa(b) {
            b = a(b);
            var c = b.offset(),
                d = {
                    left: c.left,
                    right: c.left + b.outerWidth(),
                    top: c.top,
                    bottom: c.top + b.outerHeight()
                };
            return d
        }

        function Ba(a, b) {
            return a.x > b.left && a.x < b.right && a.y > b.top && a.y < b.bottom
        }

        function Ca(a, b) {
            return a.x == b.x && a.y == b.y
        }

        var c = a.extend({}, c),
            Da = z || B || !c.fallbackToMouseEvents,
            Ea = Da ? B ? A ? "MSPointerDown" : "pointerdown" : "touchstart" : "mousedown",
            Fa = Da ? B ? A ? "MSPointerMove" : "pointermove" : "touchmove" : "mousemove",
            Ga = Da ? B ? A ? "MSPointerUp" : "pointerup" : "touchend" : "mouseup",
            Ha = Da ? B ? "mouseleave" : null : "mouseleave",
            Ia = B ? A ? "MSPointerCancel" : "pointercancel" : "touchcancel",
            Ja = 0,
            Ka = null,
            La = null,
            Ma = 0,
            Na = 0,
            Oa = 0,
            Pa = 1,
            Qa = 0,
            Ra = 0,
            Sa = null,
            Ta = a(b),
            Ua = "start",
            Va = 0,
            Wa = {},
            Xa = 0,
            Ya = 0,
            Za = 0,
            $a = 0,
            _a = 0,
            ab = null,
            bb = null;
        try {
            Ta.bind(Ea, d), Ta.bind(Ia, F)
        } catch (b) {
            a.error("events not supported " + Ea + "," + Ia + " on jQuery.swipe")
        }
        this.enable = function () {
            return this.disable(), Ta.bind(Ea, d), Ta.bind(Ia, F), Ta
        }, this.disable = function () {
            return H(), Ta
        }, this.destroy = function () {
            H(), Ta.data(C, null), Ta = null
        }, this.option = function (b, d) {
            if ("object" == typeof b) c = a.extend(c, b);
            else if (void 0 !== c[b]) {
                if (void 0 === d) return c[b];
                c[b] = d
            } else {
                if (!b) return c;
                a.error("Option " + b + " does not exist on jQuery.swipe.options")
            }
            return null
        }
    }

    var d = "1.6.18",
        e = "left",
        f = "right",
        g = "up",
        h = "down",
        i = "in",
        j = "out",
        k = "none",
        l = "auto",
        m = "swipe",
        n = "pinch",
        o = "tap",
        p = "doubletap",
        q = "longtap",
        r = "horizontal",
        s = "vertical",
        t = "all",
        u = 10,
        v = "start",
        w = "move",
        x = "end",
        y = "cancel",
        z = "ontouchstart" in window,
        A = window.navigator.msPointerEnabled && !window.navigator.pointerEnabled && !z,
        B = (window.navigator.pointerEnabled || window.navigator.msPointerEnabled) && !z,
        C = "TouchSwipe",
        D = {
            fingers: 1,
            threshold: 75,
            cancelThreshold: null,
            pinchThreshold: 20,
            maxTimeThreshold: null,
            fingerReleaseThreshold: 250,
            longTapThreshold: 500,
            doubleTapThreshold: 200,
            swipe: null,
            swipeLeft: null,
            swipeRight: null,
            swipeUp: null,
            swipeDown: null,
            swipeStatus: null,
            pinchIn: null,
            pinchOut: null,
            pinchStatus: null,
            click: null,
            tap: null,
            doubleTap: null,
            longTap: null,
            hold: null,
            triggerOnTouchEnd: !0,
            triggerOnTouchLeave: !1,
            allowPageScroll: "auto",
            fallbackToMouseEvents: !0,
            excludedElements: ".noSwipe",
            preventDefaultEvents: !0
        };
    a.fn.swipe = function (c) {
        var d = a(this),
            e = d.data(C);
        if (e && "string" == typeof c) {
            if (e[c]) return e[c].apply(e, Array.prototype.slice.call(arguments, 1));
            a.error("Method " + c + " does not exist on jQuery.swipe")
        } else if (e && "object" == typeof c) e.option.apply(e, arguments);
        else if (!(e || "object" != typeof c && c)) return b.apply(this, arguments);
        return d
    }, a.fn.swipe.version = d, a.fn.swipe.defaults = D, a.fn.swipe.phases = {
        PHASE_START: v,
        PHASE_MOVE: w,
        PHASE_END: x,
        PHASE_CANCEL: y
    }, a.fn.swipe.directions = {
        LEFT: e,
        RIGHT: f,
        UP: g,
        DOWN: h,
        IN: i,
        OUT: j
    }, a.fn.swipe.pageScroll = {
        NONE: k,
        HORIZONTAL: r,
        VERTICAL: s,
        AUTO: l
    }, a.fn.swipe.fingers = {
        ONE: 1,
        TWO: 2,
        THREE: 3,
        FOUR: 4,
        FIVE: 5,
        ALL: t
    }
}), !function (a) {
    a.isBreakpoint = function (b) {
        var c, d;
        return c = a("<div/>", {
            class: "visible-" + b
        }).appendTo("body"), d = c.is(":visible"), c.remove(), d
    }, a.extend(a, {
        isXs: function () {
            return a.isBreakpoint("xs")
        },
        isSm: function () {
            return a.isBreakpoint("sm")
        },
        isMd: function () {
            return a.isBreakpoint("md")
        },
        isLg: function () {
            return a.isBreakpoint("lg")
        }
    })
}(jQuery);
var     App = function () {
        "use strict";
        return App.dashboard = function () {
            function a() {
                $('[data-toggle="counter"]').each(function (a, b) {
                    var c = $(this),
                        d = "",
                        e = "",
                        f = 0,
                        g = 0,
                        h = 0,
                        i = 2.5;
                    c.data("prefix") && (d = c.data("prefix")), c.data("suffix") && (e = c.data("suffix")), c.data("start") && (f = c.data("start")), c.data("end") && (g = c.data("end")), c.data("decimals") && (h = c.data("decimals")), c.data("duration") && (i = c.data("duration"));
                    var j = new CountUp(c.get(0), f, g, h, i, {
                        suffix: e,
                        prefix: d
                    });
                    j.start()
                })
            }

            function d() {
                var a = [{
                        label: "Google Ads",
                        data: 70
                    }, {
                        label: "Facebook",
                        data: 10
                    }],
                    b = App.color.alt3,
                    c = tinycolor(App.color.alt4).lighten(6.5).toString();
                $.plot("#widget-top-3", a, {
                    series: {
                        pie: {
                            show: !0,
                            label: {
                                show: !1
                            },
                            highlight: {
                                opacity: .1
                            }
                        }
                    },
                    grid: {
                        hoverable: !0
                    },
                    legend: {
                        show: !1
                    },
                    colors: [b, c]
                })
            }

            a();

            var k = App.color.alt2,
                l = tinycolor(App.color.primary).lighten(5).toString();
            $("#spk1").sparkline([2, 3, 4, 5, 6, 7, 8, 9, 10, 12, 14], {
                type: "bar",
                width: "80px",
                height: "30px",
                barColor: k
            }), $("#spk2").sparkline([4, 6, 7, 7, 5, 3, 4, 6, 8, 10, 12], {
                type: "bar",
                width: "80px",
                height: "30px",
                barColor: l
            })
        }, App
    }(App || {});