Date.dayNames = ['Sunday','Monday','Tuesday','Wednesday','Thursday','Friday','Saturday'];
Date.abbrDayNames = ['Sun','Mon','Tue','Wed','Thu','Fri','Sat'];
Date.monthNames = ['January','February','March','April','May','June','July','August','September','October','November','December'];
Date.abbrMonthNames = ['Jan','Feb','Mar','Apr','May','Jun','Jul','Aug','Sep','Oct','Nov','Dec'];
Date.firstDayOfWeek = 1;
Date.format = 'dd/mm/yyyy';
(function() {
    function add(name, method) {
        if (!Date.prototype[name]) {
            Date.prototype[name] = method
        }
    }
    ;
    add("isLeapYear", function() {
        var y = this.getFullYear();
        return(y % 4 == 0 && y % 100 != 0) || y % 400 == 0
    });
    add("isWeekend", function() {
        return this.getDay() == 0 || this.getDay() == 6
    });
    add("isWeekDay", function() {
        return!this.isWeekend()
    });
    add("getDaysInMonth", function() {
        return[31,(this.isLeapYear() ? 29 : 28),31,30,31,30,31,31,30,31,30,31][this.getMonth()]
    });
    add("getDayName", function(abbreviated) {
        return abbreviated ? Date.abbrDayNames[this.getDay()] : Date.dayNames[this.getDay()]
    });
    add("getMonthName", function(abbreviated) {
        return abbreviated ? Date.abbrMonthNames[this.getMonth()] : Date.monthNames[this.getMonth()]
    });
    add("getDayOfYear", function() {
        var tmpdtm = new Date("1/1/" + this.getFullYear());
        return Math.floor((this.getTime() - tmpdtm.getTime()) / 86400000)
    });
    add("getWeekOfYear", function() {
        return Math.ceil(this.getDayOfYear() / 7)
    });
    add("setDayOfYear", function(day) {
        this.setMonth(0);
        this.setDate(day);
        return this
    });
    add("addYears", function(num) {
        this.setFullYear(this.getFullYear() + num);
        return this
    });
    add("addMonths", function(num) {
        var tmpdtm = this.getDate();
        this.setMonth(this.getMonth() + num);
        if (tmpdtm > this.getDate())this.addDays(-this.getDate());
        return this
    });
    add("addDays", function(num) {
        this.setDate(this.getDate() + num);
        return this
    });
    add("addHours", function(num) {
        this.setHours(this.getHours() + num);
        return this
    });
    add("addMinutes", function(num) {
        this.setMinutes(this.getMinutes() + num);
        return this
    });
    add("addSeconds", function(num) {
        this.setSeconds(this.getSeconds() + num);
        return this
    });
    add("zeroTime", function() {
        this.setMilliseconds(0);
        this.setSeconds(0);
        this.setMinutes(0);
        this.setHours(0);
        return this
    });
    add("asString", function() {
        var r = Date.format;
        return r.split('yyyy').join(this.getFullYear()).split('yy').join(this.getYear()).split('mmm').join(this.getMonthName(true)).split('mm').join(_zeroPad(this.getMonth() + 1)).split('dd').join(_zeroPad(this.getDate()))
    });
    Date.fromString = function(s) {
        var f = Date.format;
        var d = new Date('01/01/1977');
        var iY = f.indexOf('yyyy');
        if (iY > -1) {
            d.setFullYear(Number(s.substr(iY, 4)))
        } else {
            d.setYear(Number(s.substr(f.indexOf('yy'), 2)))
        }
        var iM = f.indexOf('mmm');
        if (iM > -1) {
            var mStr = s.substr(iM, 3);
            for (var i = 0; i < Date.abbrMonthNames.length; i++) {
                if (Date.abbrMonthNames[i] == mStr)break
            }
            d.setMonth(i)
        } else {
            d.setMonth(Number(s.substr(f.indexOf('mm'), 2)) - 1)
        }
        d.setDate(Number(s.substr(f.indexOf('dd'), 2)));
        if (isNaN(d.getTime())) {
            return false
        }
        return d
    };
    var _zeroPad = function(num) {
        var s = '0' + num;
        return s.substring(s.length - 2)
    }
})();
(function($) {
    $.fn.extend({renderCalendar:function(s) {
        var dc = function(a) {
            return document.createElement(a)
        };
        s = $.extend({month:null,year:null,renderCallback:null,showHeader:$.dpConst.SHOW_HEADER_SHORT,dpController:null,hoverClass:'dp-hover'}, s);
        if (s.showHeader != $.dpConst.SHOW_HEADER_NONE) {
            var headRow = $(dc('tr'));
            for (var i = Date.firstDayOfWeek; i < Date.firstDayOfWeek + 7; i++) {
                var weekday = i % 7;
                var day = Date.dayNames[weekday];
                headRow.append(jQuery(dc('th')).attr({'scope':'col','abbr':day,'title':day,'class':(weekday == 0 || weekday == 6 ? 'weekend' : 'weekday')}).html(s.showHeader == $.dpConst.SHOW_HEADER_SHORT ? day.substr(0, 1) : day))
            }
        }
        ;
        var calendarTable = $(dc('table')).attr({'cellspacing':2,'className':'jCalendar'}).append((s.showHeader != $.dpConst.SHOW_HEADER_NONE ? $(dc('thead')).append(headRow) : dc('thead')));
        var tbody = $(dc('tbody'));
        var today = (new Date()).zeroTime();
        var month = s.month == undefined ? today.getMonth() : s.month;
        var year = s.year || today.getFullYear();
        var currentDate = new Date(year, month, 1);
        var firstDayOffset = Date.firstDayOfWeek - currentDate.getDay() + 1;
        if (firstDayOffset > 1)firstDayOffset -= 7;
        currentDate.addDays(firstDayOffset - 1);
        var doHover = function() {
            if (s.hoverClass) {
                $(this).addClass(s.hoverClass)
            }
        };
        var unHover = function() {
            if (s.hoverClass) {
                $(this).removeClass(s.hoverClass)
            }
        };
        var w = 0;
        while (w++ < 6) {
            var r = jQuery(dc('tr'));
            for (var i = 0; i < 7; i++) {
                var thisMonth = currentDate.getMonth() == month;
                var d = $(dc('td')).text(currentDate.getDate() + '').attr('className', (thisMonth ? 'current-month ' : 'other-month ') + (currentDate.isWeekend() ? 'weekend ' : 'weekday ') + (thisMonth && currentDate.getTime() == today.getTime() ? 'today ' : '')).hover(doHover, unHover);
                if (s.renderCallback) {
                    s.renderCallback(d, currentDate, month, year)
                }
                r.append(d);
                currentDate.addDays(1)
            }
            tbody.append(r)
        }
        calendarTable.append(tbody);
        return this.each(function() {
            $(this).empty().append(calendarTable)
        })
    },datePicker:function(s) {
        if (!$.event._dpCache)$.event._dpCache = [];
        s = $.extend({month:undefined,year:undefined,startDate:undefined,endDate:undefined,renderCallback:[],createButton:true,showYearNavigation:true,closeOnSelect:true,displayClose:false,selectMultiple:false,clickInput:false,verticalPosition:$.dpConst.POS_TOP,horizontalPosition:$.dpConst.POS_LEFT,verticalOffset:0,horizontalOffset:0,hoverClass:'dp-hover'}, s);
        return this.each(function() {
            var $this = $(this);
            if (!this._dpId) {
                this._dpId = $.event.guid++;
                $.event._dpCache[this._dpId] = new DatePicker(this)
            }
            var controller = $.event._dpCache[this._dpId];
            controller.init(s);
            if (s.createButton) {
                controller.button = $('<a href="#" class="dp-choose-date" title="' + $.dpText.TEXT_CHOOSE_DATE + '">' + $.dpText.TEXT_CHOOSE_DATE + '</a>').bind('click', function() {
                    $this.dpDisplay(this);
                    this.blur();
                    return false
                });
                $this.after(controller.button)
            }
            if ($this.is(':text')) {
                $this.bind('dateSelected', function(e, selectedDate, $td) {
                    this.value = selectedDate.asString()
                }).bind('change', function() {
                    var d = Date.fromString(this.value);
                    if (d) {
                        controller.setSelected(d, true, true)
                    }
                });
                if (s.clickInput) {
                    $this.bind('click', function() {
                        $this.dpDisplay()
                    })
                }
            }
            $this.addClass('dp-applied')
        })
    },dpSetDisabled:function(s) {
        return _w.call(this, 'setDisabled', s)
    },dpSetStartDate:function(d) {
        return _w.call(this, 'setStartDate', d)
    },dpSetEndDate:function(d) {
        return _w.call(this, 'setEndDate', d)
    },dpGetSelected:function() {
        var c = _getController(this[0]);
        if (c) {
            return c.getSelected()
        }
        return null
    },dpSetSelected:function(d, v, m) {
        if (v == undefined)v = true;
        if (m == undefined)m = true;
        return _w.call(this, 'setSelected', Date.fromString(d), v, m)
    },dpSetDisplayedMonth:function(m, y) {
        return _w.call(this, 'setDisplayedMonth', Number(m), Number(y))
    },dpDisplay:function(e) {
        return _w.call(this, 'display', e)
    },dpSetRenderCallback:function(a) {
        return _w.call(this, 'setRenderCallback', a)
    },dpSetPosition:function(v, h) {
        return _w.call(this, 'setPosition', v, h)
    },dpSetOffset:function(v, h) {
        return _w.call(this, 'setOffset', v, h)
    },_dpDestroy:function() {
    }});
    var _w = function(f, a1, a2, a3) {
        return this.each(function() {
            var c = _getController(this);
            if (c) {
                c[f](a1, a2, a3)
            }
        })
    };
    function DatePicker(ele) {
        this.ele = ele;
        this.displayedMonth = null;
        this.displayedYear = null;
        this.startDate = null;
        this.endDate = null;
        this.showYearNavigation = null;
        this.closeOnSelect = null;
        this.displayClose = null;
        this.selectMultiple = null;
        this.verticalPosition = null;
        this.horizontalPosition = null;
        this.verticalOffset = null;
        this.horizontalOffset = null;
        this.button = null;
        this.renderCallback = [];
        this.selectedDates = {}
    }
    ;
    $.extend(DatePicker.prototype, {init:function(s) {
        this.setStartDate(s.startDate);
        this.setEndDate(s.endDate);
        this.setDisplayedMonth(Number(s.month), Number(s.year));
        this.setRenderCallback(s.renderCallback);
        this.showYearNavigation = s.showYearNavigation;
        this.closeOnSelect = s.closeOnSelect;
        this.displayClose = s.displayClose;
        this.selectMultiple = s.selectMultiple;
        this.verticalPosition = s.verticalPosition;
        this.horizontalPosition = s.horizontalPosition;
        this.hoverClass = s.hoverClass;
        this.setOffset(s.verticalOffset, s.horizontalOffset)
    },setStartDate:function(d) {
        if (d) {
            this.startDate = Date.fromString(d)
        }
        if (!this.startDate) {
            this.startDate = (new Date()).zeroTime()
        }
        this.setDisplayedMonth(this.displayedMonth, this.displayedYear)
    },setEndDate:function(d) {
        if (d) {
            this.endDate = Date.fromString(d)
        }
        if (!this.endDate) {
            this.endDate = (new Date('12/31/2999'))
        }
        if (this.endDate.getTime() < this.startDate.getTime()) {
            this.endDate = this.startDate
        }
        this.setDisplayedMonth(this.displayedMonth, this.displayedYear)
    },setPosition:function(v, h) {
        this.verticalPosition = v;
        this.horizontalPosition = h
    },setOffset:function(v, h) {
        this.verticalOffset = parseInt(v) || 0;
        this.horizontalOffset = parseInt(h) || 0
    },setDisabled:function(s) {
        $e = $(this.ele);
        $e[s ? 'addClass' : 'removeClass']('dp-disabled');
        if (this.button) {
            $but = $(this.button);
            $but[s ? 'addClass' : 'removeClass']('dp-disabled');
            $but.attr('title', s ? '' : $.dpText.TEXT_CHOOSE_DATE)
        }
        if ($e.is(':text')) {
            $e.attr('disabled', s ? 'disabled' : '')
        }
    },setDisplayedMonth:function(m, y) {
        if (this.startDate == undefined || this.endDate == undefined) {
            return
        }
        var s = new Date(this.startDate.getTime());
        s.setDate(1);
        var e = new Date(this.endDate.getTime());
        e.setDate(1);
        var t;
        if (isNaN(m) && isNaN(y)) {
            t = new Date().zeroTime();
            t.setDate(1)
        } else if (isNaN(m)) {
            t = new Date(y, this.displayedMonth, 1)
        } else if (isNaN(y)) {
            t = new Date(this.displayedYear, m, 1)
        } else {
            t = new Date(y, m, 1)
        }
        if (t.getTime() < s.getTime()) {
            t = s
        } else if (t.getTime() > e.getTime()) {
            t = e
        }
        this.displayedMonth = t.getMonth();
        this.displayedYear = t.getFullYear()
    },setSelected:function(d, v, moveToMonth) {
        if (this.selectMultiple == false) {
            this.selectedDates = {}
        }
        if (moveToMonth) {
            this.setDisplayedMonth(d.getMonth(), d.getFullYear())
        }
        this.selectedDates[d.getTime()] = v
    },isSelected:function(t) {
        return this.selectedDates[t]
    },getSelected:function() {
        var r = [];
        for (t in this.selectedDates) {
            if (this.selectedDates[t] == true) {
                r.push(new Date(Number(t)))
            }
        }
        return r
    },display:function(eleAlignTo) {
        if ($(this.ele).is('.dp-disabled'))return;
        eleAlignTo = eleAlignTo || this.ele;
        var c = this;
        var $ele = $(eleAlignTo);
        var eleOffset = $ele.offset();
        var _checkMouse = function(e) {
            var el = e.target;
            var cal = $('#dp-popup')[0];
            while (true) {
                if (el == cal) {
                    return true
                } else if (el == document) {
                    c._closeCalendar();
                    return false
                } else {
                    el = $(el).parent()[0]
                }
            }
        };
        this._checkMouse = _checkMouse;
        this._closeCalendar(true);
        $('body').append($('<div></div>').attr('id', 'dp-popup').css({'top':eleOffset.top + c.verticalOffset,'left':eleOffset.left + c.horizontalOffset}).append($('<h2></h2>'), $('<div id="dp-nav-prev"></div>').append($('<a id="dp-nav-prev-year" href="#" title="' + $.dpText.TEXT_PREV_YEAR + '">&lt;&lt;</a>').bind('click', function() {
            return c._displayNewMonth.call(c, this, 0, -1)
        }), $('<a id="dp-nav-prev-month" href="#" title="' + $.dpText.TEXT_PREV_MONTH + '">&lt;</a>').bind('click', function() {
            return c._displayNewMonth.call(c, this, -1, 0)
        })), $('<div id="dp-nav-next"></div>').append($('<a id="dp-nav-next-year" href="#" title="' + $.dpText.TEXT_NEXT_YEAR + '">&gt;&gt;</a>').bind('click', function() {
            return c._displayNewMonth.call(c, this, 0, 1)
        }), $('<a id="dp-nav-next-month" href="#" title="' + $.dpText.TEXT_NEXT_MONTH + '">&gt;</a>').bind('click', function() {
            return c._displayNewMonth.call(c, this, 1, 0)
        })), $('<div></div>').attr('id', 'dp-calendar')).bgIframe());
        var $pop = $('#dp-popup');
        if (this.showYearNavigation == false) {
            $('#dp-nav-prev-year, #dp-nav-next-year').css('display', 'none')
        }
        if (this.displayClose) {
            $pop.append($('<a href="#" id="dp-close">' + $.dpText.TEXT_CLOSE + '</a>').bind('click', function() {
                c._closeCalendar();
                return false
            }))
        }
        c._renderCalendar();
        if (this.verticalPosition == $.dpConst.POS_BOTTOM) {
            $pop.css('top', eleOffset.top + $ele.height() - $pop.height() + c.verticalOffset)
        }
        if (this.horizontalPosition == $.dpConst.POS_RIGHT) {
            $pop.css('left', eleOffset.left + $ele.width() - $pop.width() + c.horizontalOffset)
        }
        $(this.ele).trigger('dpDisplayed', $pop);
        $(document).bind('mousedown', this._checkMouse)
    },setRenderCallback:function(a) {
        if (a && typeof(a) == 'function') {
            a = [a]
        }
        this.renderCallback = this.renderCallback.concat(a)
    },cellRender:function($td, thisDate, month, year) {
        var c = this.dpController;
        var d = new Date(thisDate.getTime());
        $td.bind('click', function() {
            var $this = $(this);
            if (!$this.is('.disabled')) {
                c.setSelected(d, !$this.is('.selected') || !c.selectMultiple);
                var s = c.isSelected(d.getTime());
                $(c.ele).trigger('dateSelected', [d,$td,s]);
                if (c.closeOnSelect) {
                    c._closeCalendar()
                } else {
                    $this[s ? 'addClass' : 'removeClass']('selected')
                }
            }
        });
        if (c.isSelected(d.getTime())) {
            $td.addClass('selected')
        }
        for (var i = 0; i < c.renderCallback.length; i++) {
            c.renderCallback[i].apply(this, arguments)
        }
    },_displayNewMonth:function(ele, m, y) {
        if (!$(ele).is('.disabled')) {
            this.setDisplayedMonth(this.displayedMonth + m, this.displayedYear + y);
            this._clearCalendar();
            this._renderCalendar();
            $(this.ele).trigger('dpMonthChanged', [this.displayedMonth,this.displayedYear])
        }
        ele.blur();
        return false
    },_renderCalendar:function() {
        $('#dp-popup h2').html(Date.monthNames[this.displayedMonth] + ' ' + this.displayedYear);
        $('#dp-calendar').renderCalendar({month:this.displayedMonth,year:this.displayedYear,renderCallback:this.cellRender,dpController:this,hoverClass:this.hoverClass});
        if (this.displayedYear == this.startDate.getFullYear() && this.displayedMonth == this.startDate.getMonth()) {
            $('#dp-nav-prev-year').addClass('disabled');
            $('#dp-nav-prev-month').addClass('disabled');
            $('#dp-calendar td.other-month').each(function() {
                var $this = $(this);
                if (Number($this.text()) > 20) {
                    $this.addClass('disabled')
                }
            });
            var d = this.startDate.getDate();
            $('#dp-calendar td.current-month').each(function() {
                var $this = $(this);
                if (Number($this.text()) < d) {
                    $this.addClass('disabled')
                }
            })
        } else {
            $('#dp-nav-prev-year').removeClass('disabled');
            $('#dp-nav-prev-month').removeClass('disabled');
            var d = this.startDate.getDate();
            if (d > 20) {
                var sd = new Date(this.startDate.getTime());
                sd.addMonths(1);
                if (this.displayedYear == sd.getFullYear() && this.displayedMonth == sd.getMonth()) {
                    $('#dp-calendar td.other-month').each(function() {
                        var $this = $(this);
                        if (Number($this.text()) < d) {
                            $this.addClass('disabled')
                        }
                    })
                }
            }
        }
        if (this.displayedYear == this.endDate.getFullYear() && this.displayedMonth == this.endDate.getMonth()) {
            $('#dp-nav-next-year').addClass('disabled');
            $('#dp-nav-next-month').addClass('disabled');
            $('#dp-calendar td.other-month').each(function() {
                var $this = $(this);
                if (Number($this.text()) < 14) {
                    $this.addClass('disabled')
                }
            });
            var d = this.endDate.getDate();
            $('#dp-calendar td.current-month').each(function() {
                var $this = $(this);
                if (Number($this.text()) > d) {
                    $this.addClass('disabled')
                }
            })
        } else {
            $('#dp-nav-next-year').removeClass('disabled');
            $('#dp-nav-next-month').removeClass('disabled');
            var d = this.endDate.getDate();
            if (d < 13) {
                var ed = new Date(this.endDate.getTime());
                ed.addMonths(-1);
                if (this.displayedYear == ed.getFullYear() && this.displayedMonth == ed.getMonth()) {
                    $('#dp-calendar td.other-month').each(function() {
                        var $this = $(this);
                        if (Number($this.text()) > d) {
                            $this.addClass('disabled')
                        }
                    })
                }
            }
        }
    },_closeCalendar:function(programatic) {
        $(document).unbind('mousedown', this._checkMouse);
        this._clearCalendar();
        $('#dp-popup a').unbind();
        $('#dp-popup').empty().remove();
        if (!programatic) {
            $(this.ele).trigger('dpClosed', [this.getSelected()])
        }
    },_clearCalendar:function() {
        $('#dp-calendar td').unbind();
        $('#dp-calendar').empty()
    }});
    $.dpConst = {SHOW_HEADER_NONE:0,SHOW_HEADER_SHORT:1,SHOW_HEADER_LONG:2,POS_TOP:0,POS_BOTTOM:1,POS_LEFT:0,POS_RIGHT:1};
    $.dpText = {TEXT_PREV_YEAR:'Previous year',TEXT_PREV_MONTH:'Previous month',TEXT_NEXT_YEAR:'Next year',TEXT_NEXT_MONTH:'Next month',TEXT_CLOSE:'Close',TEXT_CHOOSE_DATE:'Choose date'};
    $.dpVersion = '$Id: datePicker.js,v 1.2 2007/09/06 13:09:30 BELHARD\MeleshkoDN Exp $';
    function _getController(ele) {
        if (ele._dpId)return $.event._dpCache[ele._dpId];
        return false
    }
    ;
    if ($.fn.bgIframe == undefined) {
        $.fn.bgIframe = function() {
            return this
        }
    }
    ;
    $(window).bind('unload', function() {
        var els = $.event._dpCache || [];
        for (var i in els) {
            $(els[i].ele)._dpDestroy()
        }
    })
})(jQuery);
(function($) {
    $.fn.bgIframe = $.fn.bgiframe = function(s) {
        if ($.browser.msie && /6.0/.test(navigator.userAgent)) {
            s = $.extend({top:'auto',left:'auto',width:'auto',height:'auto',opacity:true,src:'javascript:false;'}, s || {});
            var prop = function(n) {
                return n && n.constructor == Number ? n + 'px' : n
            },html = '<iframe class="bgiframe"frameborder="0"tabindex="-1"src="' + s.src + '"' + 'style="display:block;position:absolute;z-index:-1;' + (s.opacity !== false ? 'filter:Alpha(Opacity=\'0\');' : '') + 'top:' + (s.top == 'auto' ? 'expression(((parseInt(this.parentNode.currentStyle.borderTopWidth)||0)*-1)+\'px\')' : prop(s.top)) + ';' + 'left:' + (s.left == 'auto' ? 'expression(((parseInt(this.parentNode.currentStyle.borderLeftWidth)||0)*-1)+\'px\')' : prop(s.left)) + ';' + 'width:' + (s.width == 'auto' ? 'expression(this.parentNode.offsetWidth+\'px\')' : prop(s.width)) + ';' + 'height:' + (s.height == 'auto' ? 'expression(this.parentNode.offsetHeight+\'px\')' : prop(s.height)) + ';' + '"/>';
            return this.each(function() {
                if ($('> iframe.bgiframe', this).length == 0){
                    var htel = document.createElement("iframe");
                    htel.setAttribute("class", "bgiframe");
                    htel.setAttribute("frameborder", "0");
                    htel.setAttribute("tabindex", "-1");
                    htel.setAttribute("src", s.src);
                    htel.setAttribute("style", "display:block;position:absolute;z-index:-1;" + (s.opacity !== false ? "filter:Alpha(Opacity='0');" : "") + "top:" + (s.top == 'auto' ? 'expression(((parseInt(this.parentNode.currentStyle.borderTopWidth)||0)*-1)+\'px\')' : prop(s.top)) + ';' + 'left:' + (s.left == 'auto' ? 'expression(((parseInt(this.parentNode.currentStyle.borderLeftWidth)||0)*-1)+\'px\')' : prop(s.left)) + ';' + 'width:' + (s.width == 'auto' ? 'expression(this.parentNode.offsetWidth+\'px\')' : prop(s.width)) + ';' + 'height:' + (s.height == 'auto' ? 'expression(this.parentNode.offsetHeight+\'px\')' : prop(s.height)) + ';');

                    this.insertBefore(htel, this.firstChild);
                }
            });
        }
        return this;
    }
})(jQuery);
jQuery.fn.ajaxSubmit = function(options) {
    if (typeof options == 'function')options = {success:options};
    options = jQuery.extend({url:this.attr('action') || '',method:this.attr('method') || 'GET'}, options || {});
    options.success = options.success || options.after;
    options.beforeSubmit = options.beforeSubmit || options.before;
    options.type = options.type || options.method;
    var a = this.formToArray(options.semantic);
    if (options.beforeSubmit && options.beforeSubmit(a, this, options) === false)return this;
    var veto = {};
    jQuery.event.trigger('form.submit.validate', [a,this,options,veto]);
    if (veto.veto)return this;
    var q = jQuery.param(a);
    if (options.type.toUpperCase() == 'GET') {
        options.url += (options.url.indexOf('?') >= 0 ? '&' : '?') + q;
        options.data = null
    } else options.data = q;
    var $form = this,callbacks = [];
    if (options.resetForm)callbacks.push(function() {
        $form.resetForm()
    });
    if (options.clearForm)callbacks.push(function() {
        $form.clearForm()
    });
    if (!options.dataType && options.target) {
        var oldSuccess = options.success || function() {
        };
        callbacks.push(function(data, status) {
            jQuery(options.target).attr("innerHTML", data).evalScripts().each(oldSuccess, [data,status])
        })
    } else if (options.success)callbacks.push(options.success);
    options.success = function(data, status) {
        for (var i = 0,max = callbacks.length; i < max; i++)callbacks[i](data, status)
    };
    jQuery.event.trigger('form.submit.notify', [this,options]);
    jQuery.ajax(options);
    return this
};
jQuery.fn.ajaxForm = function(options) {
    return this.each(function() {
        jQuery("input:submit,input:image,button:submit", this).click(function(ev) {
            var $form = this.form;
            $form.clk = this;
            if (this.type == 'image') {
                if (ev.offsetX != undefined) {
                    $form.clk_x = ev.offsetX;
                    $form.clk_y = ev.offsetY
                } else if (typeof jQuery.fn.offset == 'function') {
                    var offset = jQuery(this).offset();
                    $form.clk_x = ev.pageX - offset.left;
                    $form.clk_y = ev.pageY - offset.top
                } else {
                    $form.clk_x = ev.pageX - this.offsetLeft;
                    $form.clk_y = ev.pageY - this.offsetTop
                }
            }
            setTimeout(function() {
                $form.clk = $form.clk_x = $form.clk_y = null
            }, 10)
        })
    }).submit(function(e) {
        jQuery(this).ajaxSubmit(options);
        return false
    })
};
jQuery.fn.formToArray = function(semantic) {
    var a = [];
    if (this.length == 0)return a;
    var form = this[0];
    var els = semantic ? form.getElementsByTagName('*') : form.elements;
    if (!els)return a;
    for (var i = 0,max = els.length; i < max; i++) {
        var el = els[i];
        var n = el.name;
        if (!n)continue;
        if (semantic && form.clk && el.type == "image") {
            if (!el.disabled && form.clk == el)a.push({name:n + '.x',value:form.clk_x}, {name:n + '.y',value:form.clk_y});
            continue
        }
        var v = jQuery.fieldValue(el, true);
        if (v === null)continue;
        if (v.constructor == Array) {
            for (var j = 0,jmax = v.length; j < jmax; j++)a.push({name:n,value:v[j]})
        } else a.push({name:n,value:v})
    }
    if (!semantic && form.clk) {
        var inputs = form.getElementsByTagName("input");
        for (var i = 0,max = inputs.length; i < max; i++) {
            var input = inputs[i];
            var n = input.name;
            if (n && !input.disabled && input.type == "image" && form.clk == input)a.push({name:n + '.x',value:form.clk_x}, {name:n + '.y',value:form.clk_y})
        }
    }
    return a
};
jQuery.fn.formSerialize = function(semantic) {
    return jQuery.param(this.formToArray(semantic))
};
jQuery.fn.fieldSerialize = function(successful) {
    var a = [];
    this.each(function() {
        var n = this.name;
        if (!n)return;
        var v = jQuery.fieldValue(this, successful);
        if (v && v.constructor == Array) {
            for (var i = 0,max = v.length; i < max; i++)a.push({name:n,value:v[i]})
        } else if (v !== null && typeof v != 'undefined')a.push({name:this.name,value:v})
    });
    return jQuery.param(a)
};
jQuery.fn.fieldValue = function(successful) {
    var val = [],name;
    for (var i = 0,max = this.length; i < max; i++) {
        var el = this[i];
        var v = jQuery.fieldValue(el, successful);
        if (v === null || typeof v == 'undefined' || (v.constructor == Array && !v.length))continue;
        name = name || el.name;
        if (name != el.name)continue;
        v.constructor == Array ? jQuery.merge(val, v) : val.push(v)
    }
    return val
};
jQuery.fieldValue = function(el, successful) {
    var n = el.name,t = el.type,tag = el.tagName.toLowerCase();
    if (typeof successful == 'undefined')successful = true;
    if (successful && (!n || el.disabled || t == 'reset' || (t == 'checkbox' || t == 'radio') && !el.checked || (t == 'submit' || t == 'image') && el.form && el.form.clk != el || tag == 'select' && el.selectedIndex == -1))return null;
    if (tag == 'select') {
        var index = el.selectedIndex;
        if (index < 0)return null;
        var a = [],ops = el.options;
        var one = (t == 'select-one');
        var max = (one ? index + 1 : ops.length);
        for (var i = (one ? index : 0); i < max; i++) {
            var op = ops[i];
            if (op.selected) {
                var v = jQuery.browser.msie && !(op.attributes['value'].specified) ? op.text : op.value;
                if (one)return v;
                a.push(v)
            }
        }
        return a
    }
    return el.value
};
jQuery.fn.clearForm = function() {
    return this.each(function() {
        jQuery('input,select,textarea', this).clearFields()
    })
};
jQuery.fn.clearFields = jQuery.fn.clearInputs = function() {
    return this.each(function() {
        var t = this.type,tag = this.tagName.toLowerCase();
        if (t == 'text' || t == 'password' || tag == 'textarea')this.value = ''; else if (t == 'checkbox' || t == 'radio')this.checked = false; else if (tag == 'select')this.selectedIndex = -1
    })
};
jQuery.fn.resetForm = function() {
    return this.each(function() {
        if (typeof this.reset == 'function' || (typeof this.reset == 'object' && !this.reset.nodeType))this.reset()
    })
};
var w;
function printImage(img, width, height) {
    img = img.src ? img.src : img;
    if (w && !w.closed) {
        w.close()
    }
    w = open('', 'imagePrint', 'menubar=1,locationbar=0,statusbar=0,resizable=1' + ',scrollbars=1,width=' + width + ',height=' + height + '\'');
    var html = '<HTML>';
    html += '<BODY ONLOAD="if (window.print) window.print(); setTimeout(\'window.close()\', 10000);">';
    html += '<table><tr><td>';
    html += '<IMG SRC="' + img + '">';
    html += '</td></tr></table>';
    html += '<\/BODY>';
    html += '<\/HTML>';
    w.document.open();
    w.document.write(html);
    w.document.close()
}
$(function() {
    $("ul.ec_dropdown li").hover(function() {
        $(this).addClass("cat_link_hover");
        $('ul:first', this).css('visibility', 'visible')
    }, function() {
        $(this).removeClass("cat_link_hover");
        $('ul:first', this).css('visibility', 'hidden')
    });
    $("ul.ec_dropdown li ul li:has(ul)").find("a:first").append(" &raquo; ")
});
jQuery.fn.extend({everyTime:function(interval, label, fn, times, belay) {
    return this.each(function() {
        jQuery.timer.add(this, interval, label, fn, times, belay)
    })
},oneTime:function(interval, label, fn) {
    return this.each(function() {
        jQuery.timer.add(this, interval, label, fn, 1)
    })
},stopTime:function(label, fn) {
    return this.each(function() {
        jQuery.timer.remove(this, label, fn)
    })
}});
jQuery.extend({timer:{guid:1,global:{},regex:/^([0-9]+)\s*(.*s)?$/,powers:{'ms':1,'cs':10,'ds':100,'s':1000,'das':10000,'hs':100000,'ks':1000000},timeParse:function(value) {
    if (value == undefined || value == null)return null;
    var result = this.regex.exec(jQuery.trim(value.toString()));
    if (result[2]) {
        var num = parseInt(result[1], 10);
        var mult = this.powers[result[2]] || 1;
        return num * mult
    } else {
        return value
    }
},add:function(element, interval, label, fn, times, belay) {
    var counter = 0;
    if (jQuery.isFunction(label)) {
        if (!times)times = fn;
        fn = label;
        label = interval
    }
    interval = jQuery.timer.timeParse(interval);
    if (typeof interval != 'number' || isNaN(interval) || interval <= 0)return;
    if (times && times.constructor != Number) {
        belay = !!times;
        times = 0
    }
    times = times || 0;
    belay = belay || false;
    if (!element.$timers)element.$timers = {};
    if (!element.$timers[label])element.$timers[label] = {};
    fn.$timerID = fn.$timerID || this.guid++;
    var handler = function() {
        if (belay && this.inProgress)return;
        this.inProgress = true;
        if ((++counter > times && times !== 0) || fn.call(element, counter) === false)jQuery.timer.remove(element, label, fn);
        this.inProgress = false
    };
    handler.$timerID = fn.$timerID;
    if (!element.$timers[label][fn.$timerID])element.$timers[label][fn.$timerID] = window.setInterval(handler, interval);
    if (!this.global[label])this.global[label] = [];
    this.global[label].push(element)
},remove:function(element, label, fn) {
    var timers = element.$timers,ret;
    if (timers) {
        if (!label) {
            for (label in timers)this.remove(element, label, fn)
        } else if (timers[label]) {
            if (fn) {
                if (fn.$timerID) {
                    window.clearInterval(timers[label][fn.$timerID]);
                    delete timers[label][fn.$timerID]
                }
            } else {
                for (var fn in timers[label]) {
                    window.clearInterval(timers[label][fn]);
                    delete timers[label][fn]
                }
            }
            for (ret in timers[label])break;
            if (!ret) {
                ret = null;
                delete timers[label]
            }
        }
        for (ret in timers)break;
        if (!ret)element.$timers = null
    }
}}});
if (jQuery.browser.msie)jQuery(window).one("unload", function() {
    var global = jQuery.timer.global;
    for (var label in global) {
        var els = global[label],i = els.length;
        while (--i)jQuery.timer.remove(els[i], label)
    }
});