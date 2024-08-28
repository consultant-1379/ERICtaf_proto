
define('text!widgets/DateTimePicker/_dateTimePicker.html',[],function () { return '<div data-namespace="ebDateTimePicker">\n    <div data-name="dateHolder"></div>\n    <div data-name="timeHolder"></div>\n</div>';});

/*global define*/
define('widgets/utils/dataNameUtils',[
    'jscore/core'
], function (core) {
    

    return {
        prefix: '',

        translate: function(prefix, template, view) {
            if ((typeof template) === 'string') {
                template = core.Element.parse(template);
            }
            this.prefix = prefix;
            this.translateElement.call(this, template, view, true);
            this.translateChildren.call(this, template, view);
            return template;
        },

        translateElement: function(element, view, isRoot) {
            if (isRoot) {
                if (this.prefix === undefined || this.prefix === null) {
                    this.prefix = element.getAttribute('data-namespace');
                    element._getHTMLElement().removeAttribute('data-namespace');
                    element.setAttribute('class', this.prefix);
                }
            }

            var name = element.getAttribute('data-name');
            var type = element.getAttribute('data-type');

            var classes = element.getAttribute('class');

            if (classes !== undefined) {
                classes = classes.split(' ');
            } else {
                classes = [];
            }

            if (type) {
                classes.push(this.namespace + '-' + type);
                element._getHTMLElement().removeAttribute('data-type');
                if (view[type] === undefined) {
                    view[type] = [];
                }

                view[type].push(element);
            }

            if (name) {
                classes.push(this.prefix + '-' + name);
                // Prevents a conflicts with existing methods
                if (view[name] === undefined) {
                    view[name] = element;
                } else {
                    throw new Error('Name already in use: ' + name);
                }
                element._getHTMLElement().removeAttribute('data-name');
            }

            element.setAttribute('class', classes.join(' '));

            if (isRoot && name) {
                this.prefix = this.prefix + '-' + name;
            }
        },

        translateChildren: function(template, view) {
            var children = template.children();

            for (var i = 0, l = children.length; i < l; i++) {
                var child = children[i];
                this.translateElement.call(this, child, view, false);

                if (child.children().length > 0) {
                    this.translateChildren.call(this, child, view);
                }
            }
        }
    };

});

/*global define*/
define('widgets/DateTimePicker/DateTimePickerView',[
    'jscore/core',
    'text!./_dateTimePicker.html',
    'widgets/utils/dataNameUtils'
], function (core, template, dataNameUtils) {
    

    var DateTimeView = core.View.extend({

        getTemplate: function () {
            return dataNameUtils.translate(null, template, this);
        },

        getDateHolder: function () {
            return this[DateTimeView.EL_DATE_HOLDER];
        },

        getTimeHolder: function () {
            return this[DateTimeView.EL_TIME_HOLDER];
        }

    }, {
        'EL_DATE_HOLDER': 'dateHolder',
        'EL_TIME_HOLDER': 'timeHolder'
    });

    return DateTimeView;

});

define('text!widgets/DatePicker/_datePicker.html',[],function () { return '<div data-namespace="ebDatePicker">\n    <div data-name="head">\n        <div data-name="prev">\n            <div data-name="prevYear"></div>\n            <div data-name="prevMonth"></div>\n        </div>\n        <div data-name="monthYear"></div>\n        <div data-name="next">\n            <div data-name="nextYear"></div>\n            <div data-name="nextMonth"></div>\n        </div>\n    </div>\n    <div data-name="tableHolder"></div>\n</div>';});

/*global define*/
define('widgets/DatePicker/DatePickerView',[
    'widgets/main',
    'text!./_datePicker.html',
    'widgets/utils/dataNameUtils'
], function (core, template, dataNameUtils) {
    

    var DatePickerView = core.View.extend({

        getTemplate: function() {
            return dataNameUtils.translate(null, template, this);
        },

        getRoot: function() {
            return this.getElement();
        },

        getTable: function() {
            return this[DatePickerView.EL_TABLE];
        },

        getDays: function () {
            return this[DatePickerView.EL_DAYS];
        },

        getPrevYearButton: function() {
            return this[DatePickerView.EL_PREV_YEAR];
        },

        getPrevMonthButton: function() {
            return this[DatePickerView.EL_PREV_MONTH];
        },

        getMonthYearTitle: function() {
            return this[DatePickerView.EL_MONTH_YEAR];
        },

        getNextMonthButton: function() {
            return this[DatePickerView.EL_NEXT_MONTH];
        },

        getNextYearButton: function() {
            return this[DatePickerView.EL_NEXT_YEAR];
        }

    }, {
        'EL_PREV_YEAR': 'prevYear',
        'EL_PREV_MONTH': 'prevMonth',
        'EL_MONTH_YEAR': 'monthYear',
        'EL_NEXT_MONTH': 'nextMonth',
        'EL_NEXT_YEAR': 'nextYear',
        'EL_TABLE': 'tableHolder',
        'EL_DAYS': 'days'
    });

    return DatePickerView;

});

define('template!widgets/DatePicker/daysrows/_daysRows.html',['jscore/handlebars/handlebars'],function (Handlebars) { return Handlebars.template(function (Handlebars,depth0,helpers,partials,data) {
  helpers = helpers || Handlebars.helpers;
  var buffer = "", stack1, stack2, foundHelper, tmp1, self=this, functionType="function", helperMissing=helpers.helperMissing, undef=void 0, escapeExpression=this.escapeExpression;

function program1(depth0,data) {
  
  var buffer = "", stack1, stack2;
  buffer += "<tr>\n            ";
  foundHelper = helpers.days;
  stack1 = foundHelper || depth0.days;
  stack2 = helpers.each;
  tmp1 = self.program(2, program2, data);
  tmp1.hash = {};
  tmp1.fn = tmp1;
  tmp1.inverse = self.noop;
  stack1 = stack2.call(depth0, stack1, tmp1);
  if(stack1 || stack1 === 0) { buffer += stack1; }
  buffer += "\n        </tr>";
  return buffer;}
function program2(depth0,data) {
  
  var buffer = "", stack1;
  buffer += "<td><span class=\"";
  foundHelper = helpers.classes;
  stack1 = foundHelper || depth0.classes;
  if(typeof stack1 === functionType) { stack1 = stack1.call(depth0, { hash: {} }); }
  else if(stack1=== undef) { stack1 = helperMissing.call(depth0, "classes", { hash: {} }); }
  buffer += escapeExpression(stack1) + "\">";
  foundHelper = helpers.day;
  stack1 = foundHelper || depth0.day;
  if(typeof stack1 === functionType) { stack1 = stack1.call(depth0, { hash: {} }); }
  else if(stack1=== undef) { stack1 = helperMissing.call(depth0, "day", { hash: {} }); }
  buffer += escapeExpression(stack1) + "</span></td>";
  return buffer;}

  buffer += "<table class=\"ebDatePicker-body\">\n    <thead>\n    <tr>\n        <th>Mo</th>\n        <th>Tu</th>\n        <th>We</th>\n        <th>Th</th>\n        <th>Fr</th>\n        <th>Sa</th>\n        <th>Su</th>\n    </tr>\n    </thead>\n    <tbody>\n        ";
  foundHelper = helpers.weeks;
  stack1 = foundHelper || depth0.weeks;
  stack2 = helpers.each;
  tmp1 = self.program(1, program1, data);
  tmp1.hash = {};
  tmp1.fn = tmp1;
  tmp1.inverse = self.noop;
  stack1 = stack2.call(depth0, stack1, tmp1);
  if(stack1 || stack1 === 0) { buffer += stack1; }
  buffer += "\n    </tbody>\n</table>";
  return buffer;});});

/*global define*/
define('widgets/DatePicker/daysrows/DaysRowsView',[
    'widgets/main',
    'template!./_daysRows.html'
], function (core, template) {
    

    return core.View.extend({

        getTemplate: function () {
            // TODO: !!!
//            console.log(this.options.presenter.options.rows);
            return template(this.options.presenter.options.rows);
        }

    });

});
/*global define*/
define('widgets/utils/datePickerUtils',[], function () {
    

    var monthNames = [
        'January',
        'February',
        'March',
        'April',
        'May',
        'June',
        'July',
        'August',
        'September',
        'October',
        'November',
        'December'
    ];

    /**
     * The datePickerUtils used to simplify work with date and time.
     *
     * @class datePickerUtils
     * @namespace utils
     */
    var Utils = {

        /**
         * Checks if year is leap year.
         *
         * @method isLeapYear
         * @param {int} year Year to be tested
         * @return {boolean} true if provided year is leap year or false otherwise
         */
        isLeapYear: function (year) {
            // solution by Matti Virkkunen: http://stackoverflow.com/a/4881951
            return year % 4 === 0 && year % 100 !== 0 || year % 400 === 0;
        },

        /**
         * Checks if entered date is today date.
         *
         * @method isToday
         * @param {int} year
         * @param {int} month
         * @param {int} day
         * @return {boolean} true if provided triplet of year, month, day represents today, false otherwise
         */
        isToday: function (year, month, day) {
            var today = new Date();
            return today.getFullYear() === year && today.getMonth() === month && today.getDate() === day;
        },

        /**
         * Returns month name by specified range {0..11}, where 0 - January.
         *
         * @method getMonthName
         * @param {int} month Month in range {0..11} to represent month of interest, where 0 - January
         * @param {int} shortness Optional argument to specify how short the month name should be (for example 3 woul lead to Jan for January)
         * @return {String} month name by specified range and shortness
         */
        getMonthName: function (month, shortness) {
            if (!shortness) {
                return monthNames[month];
            } else {
                return monthNames[month].substr(0, shortness);
            }
        },

        /**
         * Formats specified date to human readable view in format "DD MMM YYY HH:MM:SS" (for example 12 May 2013 21:42:11)
         *
         * @method formatToHumanReadable
         * @param {Date} date Date object representing date of interest
         * @return {String} in format "DD MMM YYY HH:MM:SS" (for example "12 May 2013 21:42:11")
         */
        formatToHumanReadable: function (date) {
            if (date === null) {
                return '';
            }

            var result = [];

            var curr_date = date.getDate();
            if (curr_date < 10)
                curr_date = "0" + curr_date;

            result.push(curr_date);
            result.push(' ');
            result.push(this.getMonthName(date.getMonth(), 3));
            result.push(' ');
            result.push(date.getFullYear());
            result.push(' ');

            var curr_hour = date.getHours();
            if (curr_hour < 10)
                curr_hour = "0" + curr_hour;

            result.push(curr_hour);
            result.push(':');

            var curr_min = date.getMinutes();
            if (curr_min < 10)
                curr_min = "0" + curr_min;

            result.push(curr_min);
            result.push(':');

            var curr_sec = date.getSeconds();
            if (curr_sec < 10)
                curr_sec = "0" + curr_sec;

            result.push(curr_sec);

            return result.join('');
        },

        /**
         * Returns days amount in provided month & year.
         *
         * @method getDaysInMonth
         * @param {int} year Year of interest
         * @param {int} month Month in range {0..11} of provided year to represent month of interest, where 0 - January
         * @return {int} how many days are in provided month & year
         */
        getDaysInMonth: function (year, month) {
            return [31, this.isLeapYear(year) ? 29 : 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31][month];
        },

        /**
         * Utility needed for previous/next buttons, to figure out dates relatively, for example:
         *  <ul>
         *      <li>previous year relativeness is -12 in terms of months</li>
         *      <li>next year relativeness is 12 in terms of months</li>
         *  </ul>
         *
         * @method getMonthYearRelative
         * @param year Year of interest
         * @param month Month in range {0..11} of provided year to represent month of interest, where 0 - January
         * @param relative Relativeness in terms of months (can be also negative). For example (2012, 1, -13) would return { 2012, 0 }
         * @return {Object} {year: int, month: int}
         */
        getMonthYearRelative: function (year, month, relative) {
            var sum = month + relative;
            var absSum = Math.abs(sum);

            var candidateYear = year, candidateMonth;
            candidateYear += Math.floor(sum / 12);

            if (relative < 0 && sum < 0) {
                candidateMonth = sum % 12 + 12;

                if (candidateMonth === 12) {
                    candidateMonth = 0;
                }
            } else {
                candidateMonth = absSum % 12;
            }

            return {
                year: candidateYear,
                month: candidateMonth
            };
        },

        /**
         * Utility needed to figure out in which weekday does month start. For example:
         *  <ul>
         *      <li>(2013, 4) => 3 meaning that may in year 2013 starts on Wednesday</li>
         *  </ul>
         *
         * @method getWeekDayWhenMonthStarts
         * @param {int} year Year of interest
         * @param {int} month Month in range {0..11} of provided year to represent month of interest, where 0 - January
         * @return {int} number representing day of week in range {1..7} where 1 is Monday
         */
        getWeekDayWhenMonthStarts: function (year, month) {
            var day = new Date(year, month, 1).getDay();

            // Since getDay() return in range 0..6, where 0 is sunday, need to compensate for that
            if (day === 0) {
                return 7;
            } else {
                return day;
            }
        },

        /**
         * Specific utility to get representation of month days starting from Monday, what means that days that are
         * in previous month are replaces with *undefined* until days in month of interest start. For example:
         *  <ul>
         *      <li>(2013, 4) => [undefined, undefined, 1, 2, 3, ..., 31]</li>
         *  </ul>
         *
         * @method getDays
         * @param {int} year Year of interest
         * @param {int} month Month in range {0..11} of provided year to represent month of interest, where 0 - January
         * @return {Array} containing provided year + month day numbers prepended with undefined for the days of previous month
         */
        getDays: function (year, month) {
            var days = [];

            // To get the index of day (starting from 0) when month starts, we use -1 as weekdays are in range {1..7} and start with 1
            var startsIndex = this.getWeekDayWhenMonthStarts(year, month) - 1;
            var daysInMonth = this.getDaysInMonth(year, month);

            var dayIndex = 0;
            for (dayIndex; dayIndex < daysInMonth; dayIndex++) {
                var index = startsIndex + dayIndex;

                // As month days start with 1, not 0, we have to increment by 1 to get the actual day representation from index
                var dayValue = dayIndex + 1;

                days[index] = dayValue;
            }

            return days;
        },

        /**
         * Removes time from date
         *
         * @method removeTimeFromDate
         * @param {Date} date Date of interest
         * @return {Date} Date that has hours, minutes, seconds & milliseconds set to 0
         */
        removeTimeFromDate: function (date) {
            if (arguments.length === 0) {
                throw new Error('No object passed in for DatePickerUtils.removeTimeFromDate');
            }

            if (!(date instanceof Date)) {
                throw new Error('No date object passed in for DatePickerUtils.removeTimeFromDate');
            }

            var result = this.cloneDate(date);

            result.setHours(0);
            result.setMinutes(0);
            result.setSeconds(0);
            result.setMilliseconds(0);

            return result;
        },

        /**
         * Utility used to create new instance of provided Date object
         *
         * @method cloneDate
         * @param {Date} date
         * @return {Date} New instance of Date object representing the same date as provided
         */
        cloneDate: function (date) {
            if (date === null) {
                return date;
            }

            if (date instanceof Date) {
                // To make sure we do not change the object that was passed in
                return new Date(date.getTime());
            } else {
                throw new Error('No date object passed in for DatePickerUtils.cloneDate');
            }
        }
    };

    return Utils;

});


/*global define*/
define('widgets/DatePicker/daysrows/DaysRows',[
    'widgets/main',
    './DaysRowsView',
    'widgets/utils/datePickerUtils'
], function (core, View, datePickerUtils) {
    

    /**
     * The DaysRows class uses the Ericsson brand assets.
     *
     * @class DaysRows
     * @private
     */
    return core.Widget.extend({

        /*jshint validthis:true*/

        View: View,

        /**
         * The init method is automatically called by the constructor when using the "new" operator. If an object with
         * key/value pairs was passed into the constructor then the options variable will have those key/value pairs.
         *
         * The following options are accepted:
         *   <ul>
         *       <li>days: an array used as initial date in the DatePicker.</li>
         *       <li>selected: a date used as selected date in the DatePicker.</li>
         *       <li>_visibleYear: an int used as visible year in the DatePicker.</li>
         *       <li>_visibleMonth: an int used as visible month in the DatePicker.</li>
         *   </ul>
         *
         * @method init
         * @param {Object} options
         */
        init: function (options) {
            options = options || {};

            var days = options.days;
            if (days === undefined) {
                throw new Error('Option "days" should be defined for DaysRows widget!');
            }
            if (!(days instanceof Array)) {
                throw new Error('Option "days" should be an array');
            }

            this.dayClass = 'ebDatePicker-day';

            options.rows = {weeks: []};

            // Do loop for 6 weeks, each week has 7 days. Array from 0 to 41
            var currentDay, dayObj, weekObj;
            for (var i = 0; i < 42; i++) {
                currentDay = days[i];
                dayObj = {};

                if (!weekObj) {
                    weekObj = {days: []};
                }

                if (currentDay !== undefined) {
                    dayObj.day = currentDay;
                    dayObj.classes = this.dayClass + getModifiers.call(this, options._visibleYear, options._visibleMonth, currentDay);
                } else {
                    dayObj.day = '';
                    dayObj.classes = this.dayClass + ' ' + this.dayClass + '_other';
                }
                weekObj.days.push(dayObj);

                if ((i+1) % 7 === 0) {
                    options.rows.weeks.push(weekObj);
                    weekObj = undefined;
                }
            }

            this.options = options;
        }
    });

    /* ++++++++++++++++++++++++++++++++++++++++++ PRIVATE METHODS ++++++++++++++++++++++++++++++++++++++++++ */

    function getModifiers(year, month, day) {
        var modifierClasses = [];

        // Handle the case if disabled
        if (isDayDisabled.call(this, year, month, day)) {
            modifierClasses.push(' ');
            modifierClasses.push(this.dayClass);
            modifierClasses.push('_disabled');
        }

        // Handle the case when selected
        var selected = (this.options.selected || null);
        if (selected !== null && selected.getFullYear() === year && selected.getMonth() === month && selected.getDate() === day) {
            modifierClasses.push(' ');
            modifierClasses.push(this.dayClass);
            modifierClasses.push('_selected');
        }

        // Handle the case when is today
        if (datePickerUtils.isToday(year, month, day)) {
            modifierClasses.push(' ');
            modifierClasses.push(this.dayClass);
            modifierClasses.push('_today');
        }

        return modifierClasses.join('');
    }

    function isDayDisabled(year, month, day) {
        var result = false;

        var disableDayFn = this.options.disableDay;
        if (disableDayFn) {
            if (disableDayFn(year, month, day)) {
                result = result || true;
            }
        }

        var dateInRangeFn = this.options.dateInRange;
        if (dateInRangeFn) {
            if (!dateInRangeFn(year, month, day)) {
                result = result || true;
            }
        }

        return result;
    }

});

/*global define*/
define('widgets/utils/parserUtils',[], function () {
    

    /**
     * The parserUtils used to parse strings.
     *
     * @class parserUtils
     * @namespace utils
     */
    return {
        /**
         * Parse string to integer param
         *
         * @method parseInt
         * @param {String} value String which requires parsing
         * @return {int} Parsed integer
         */
        parseInt: function (value) {
            // Since JS parseInt is not very restrictive, we have to make our own, as "0r" for example would be treated as 0
            // see https://developer.mozilla.org/en-US/docs/JavaScript/Reference/Global_Objects/parseInt at the end

            if (/^\-?([0-9]+|Infinity)$/.test(value)) {
                return Number(value);
            }
            return NaN;
        }
    };
});
/*global define*/
define('widgets/DatePicker/ext/ext.delegate',[
    'jscore/base/jquery'
], function($) {
    

    return {
        delegate : function (element, selector, eventName, fn, context) {
            $(element._getHTMLElement()).delegate(selector, eventName, fn.bind(context));
        }
    };

});
/*global define*/
define('widgets/DatePicker/DatePicker',[
    'widgets/main',
    './DatePickerView',
    './daysrows/DaysRows',
    'widgets/utils/datePickerUtils',
    'widgets/utils/parserUtils',
    './ext/ext.delegate'
], function (core, View, DaysRows, datePickerUtils, parserUtils, delegateExt) {
    

    /**
     * The DatePicker class uses the Ericsson brand assets.
     *
     * @class DatePicker
     */
    return core.Widget.extend({
        /*jshint validthis:true*/

        View: View,

        /**
         * The init method is automatically called by the constructor when using the "new" operator. If an object with
         * key/value pairs was passed into the constructor then the options variable will have those key/value pairs.
         *
         * The following options are accepted:
         *   <ul>
         *       <li>value: a date used as initial date for DatePicker.</li>
         *       <li>dateInRange: a function used as date range for DatePicker. By default date range not defined.</li>
         *       <li>disableDay: a function used as a disabled day for DatePicker. By default a disabled day not defined.</li>
         *       <li>defaultDate: a function used as default date to show when just opening DatePicker.</li>
         *   </ul>
         *
         * @method init
         * @param {Object} options
         */
        init: function (options) {
            this.options = options || {};
        },

        /**
         * Overrides method from widget.
         * Executes every time, when added back to the screen.
         *
         * @method onViewReady
         * @private
         */
        onViewReady: function () {
            // Set default values
            this._selectedDate = null;

            var view = this.view;

            // Add handlers
            view.getPrevYearButton().addEventHandler('click', gotoPrevYear, this);
            view.getPrevMonthButton().addEventHandler('click', gotoPrevMonth, this);
            view.getNextMonthButton().addEventHandler('click', gotoNextMonth, this);
            view.getNextYearButton().addEventHandler('click', gotoNextYear, this);

            delegateExt.delegate(view.getTable(), 'span:not(.ebDatePicker-day_other):not(.ebDatePicker-day_disabled)', 'click', function (event) {
                var day = parserUtils.parseInt(event.currentTarget.textContent);
                if (day && !isNaN(day)) {
                    this._selectedDate = new Date(this._visibleYear, this._visibleMonth, day, 0, 0, 0, 0);
                    render.call(this);
                }
            }, this);

            this.setValue(this.options.value || null);
        },

        /**
         * Gets value from the DatePicker.
         *
         * @method getValue
         * @return {Date} Date object representing value in the DatePicker or null if not selected
         */
        getValue: function () {
            if (this._selectedDate !== null) {
                return datePickerUtils.cloneDate(this._selectedDate);
            }
            return this._selectedDate;
        },

        /**
         * Sets date in the DatePicker.
         *
         * @method setValue
         * @param {Date} date Date object to represent selected day in the DatePicker
         */
        setValue: function (date) {
            // To make sure we do not change the object that was passed in
            date = datePickerUtils.cloneDate(date);

            if (!(date instanceof Date)) {
                date = getDefaultDate.call(this);
            }

            if (date instanceof Date) {
                this._selectedDate = date;
            }

            // Set visible month/year so that redraw draws provided month
            this._visibleMonth = date.getMonth();
            this._visibleYear = date.getFullYear();

            // Update month, if month/year has changed
            this.redraw();
        },

        /**
         * Since DatePicker.js is only the calendar part, it is lazy and you manually have to call when you need to redraw it.
         *
         * Mainly used by DatePickerInput.js to redraw calendar part prior to showing DatePicker popup.
         *
         * @method redraw
         */
        redraw: function () {
            if (!this._visibleMonth && !this._visibleYear) {
                var today = getDefaultDate.call(this);
                redraw.call(this, today.getFullYear(), today.getMonth());
            } else {
                redraw.call(this, this._visibleYear, this._visibleMonth);
            }
        }

    });

    /* ++++++++++++++++++++++++++++++++++++++++++ PRIVATE METHODS ++++++++++++++++++++++++++++++++++++++++++ */

    function getDefaultDate() {
        var date;
        if (this.options.defaultDate && this.options.defaultDate instanceof Date) {
            date = this.options.defaultDate;
        } else if (this.options.defaultDate instanceof Function) {
            date = this.options.defaultDate();
        } else {
            date = new Date();

            // Minutes & seconds by default are 0
            date.setMinutes(0);
            date.setSeconds(0);
            date.setMilliseconds(0);
        }

        return date;
    }

    function redraw(year, month) {
        this._visibleMonth = month;
        this._visibleYear = year;

        var prevYear = datePickerUtils.getMonthYearRelative(year, month, -12);
        var prevMonth = datePickerUtils.getMonthYearRelative(year, month, -1);
        var nextMonth = datePickerUtils.getMonthYearRelative(year, month, 1);
        var nextYear = datePickerUtils.getMonthYearRelative(year, month, 12);

        // Handle disabling/enabling of previous month button
        var isPrevYearDisabled = isYearDisabled.call(this, prevYear.year);
        this.view.getPrevYearButton().setAttribute('disabled', isPrevYearDisabled);

        // Handle disabling/enabling of previous month button
        var isPrevMonthDisabled = isMonthDisabled.call(this, prevMonth.year, prevMonth.month);
        this.view.getPrevMonthButton().setAttribute('disabled', isPrevMonthDisabled);

        // Handle disabling/enabling of next month button
        var isNextMonthDisabled = isMonthDisabled.call(this, nextMonth.year, nextMonth.month);
        this.view.getNextMonthButton().setAttribute('disabled', isNextMonthDisabled);

        // Handle disabling/enabling of next month button
        var isNextYearDisabled = isYearDisabled.call(this, nextYear.year);
        this.view.getNextYearButton().setAttribute('disabled', isNextYearDisabled);

        render.call(this);
    }

    function render() {
        // Get days to display and update table
        var days = datePickerUtils.getDays(this._visibleYear, this._visibleMonth);

        // Clear data from days holder
        var children = this.view.getTable().children();
        if (children.length > 0) {
            children.forEach(function (child) {
                child.detach();
            });
        }

        // Create widget with days rows
        var daysRows = new DaysRows({
            days: days,
            _visibleYear: this._visibleYear,
            _visibleMonth: this._visibleMonth,
            dateInRange: this.options.dateInRange,
            disableDay: this.options.disableDay,
            selected: this.getValue()
        });
        this.view.getTable().append(daysRows.getElement());

        // Update month title
        var title = datePickerUtils.getMonthName(this._visibleMonth) + ' ' + this._visibleYear;
        this.view.getMonthYearTitle().setText(title);
    }

    function gotoPrevYear() {
        // Check that it is allowed to go to previous month
        var result = datePickerUtils.getMonthYearRelative(this._visibleYear, this._visibleMonth, -12);

        if (!isYearDisabled.call(this, result.year)) {
            redraw.call(this, result.year, result.month);
        }
    }

    function gotoPrevMonth() {
        // Check that it is allowed to go to previous month
        var result = datePickerUtils.getMonthYearRelative(this._visibleYear, this._visibleMonth, -1);

        if (!isMonthDisabled.call(this, result.year, result.month)) {
            redraw.call(this, result.year, result.month);
        }
    }

    function gotoNextMonth() {
        // Check that it is allowed to go to next month
        var result = datePickerUtils.getMonthYearRelative(this._visibleYear, this._visibleMonth, 1);

        if (!isMonthDisabled.call(this, result.year, result.month)) {
            redraw.call(this, result.year, result.month);
        }
    }

    function gotoNextYear() {
        // Check that it is allowed to go to next year
        var result = datePickerUtils.getMonthYearRelative(this._visibleYear, this._visibleMonth, 12);

        if (!isYearDisabled.call(this, result.year)) {
            redraw.call(this, result.year, result.month);
        }
    }

    function isYearDisabled(year) {
        var dateInRangeFn = this.options.dateInRange;
        if (dateInRangeFn) {
            if (!dateInRangeFn(year, this._visibleMonth, datePickerUtils.getDaysInMonth(year, this._visibleMonth)) && !dateInRangeFn(year, this._visibleMonth, 1)) {
                return true;
            }
        }
        return false;
    }

    function isMonthDisabled(year, month) {
        var dateInRangeFn = this.options.dateInRange;
        if (dateInRangeFn) {
            if (!dateInRangeFn(year, month, datePickerUtils.getDaysInMonth(year, month)) && !dateInRangeFn(year, month, 1)) {
                return true;
            }
        }
        return false;
    }

});

define('widgets/DatePicker', ['widgets/DatePicker/DatePicker'], function (main) { return main; });

define('text!widgets/TimePicker/_timePicker.html',[],function () { return '<div data-namespace="ebTimePicker"></div>';});

/*global define*/
define('widgets/TimePicker/TimePickerView',[
    'jscore/core',
    'text!./_timePicker.html'
], function (core, template) {
    

    return core.View.extend({

        getTemplate: function () {
            return template;
        },

        getRoot: function () {
            return this.getElement();
        }

    });

});

define('text!widgets/Spinner/_spinner.html',[],function () { return '<table data-namespace="ebSpinner">\n    <tr>\n        <td data-name="label"></td>\n        <td data-name="holder">\n            <button data-name="up" class="ebIcon ebIcon_button ebIcon_upArrow ebIcon_interactive"></button>\n            <input type="text" data-name="input" class="ebInput"/>\n            <button data-name="down" class="ebIcon ebIcon_button ebIcon_downArrow ebIcon_interactive"></button>\n        </td>\n        <td data-name="value"></td>\n    </tr>\n</table>';});

/*global define*/
define('widgets/Spinner/SpinnerView',[
    'jscore/core',
    'text!./_spinner.html',
    'widgets/utils/dataNameUtils'
], function (core, template, dataNameUtils) {
    

    var SpinnerView = core.View.extend({

        getTemplate: function () {
            return dataNameUtils.translate(null, template, this);
        },

        // TODO: Should be added to core.View and executed after render()
        afterRender: function () {
            this.ebInput = this.getElement().find('.' + SpinnerView.EL_INPUT_CLASS);
        },

        getRoot: function () {
            return this.getElement();
        },

        getPrefix: function () {
            return this[SpinnerView.EL_PREFIX];
        },

        getPostfix: function () {
            return this[SpinnerView.EL_POSTFIX];
        },

        getUpButton: function () {
            return this[SpinnerView.EL_UP_BUTTON];
        },

        getDownButton: function () {
            return this[SpinnerView.EL_DOWN_BUTTON];
        },

        getInput: function () {
            return this[SpinnerView.EL_INPUT];
        },

        getEbInput: function () {
            return this.ebInput;
        }

    }, {
        'EL_PREFIX': 'label',
        'EL_POSTFIX': 'value',
        'EL_UP_BUTTON': 'up',
        'EL_DOWN_BUTTON': 'down',
        'EL_INPUT': 'input',
        'EL_INPUT_CLASS': 'ebInput'
    });

    return SpinnerView;

});
/*global define*/
define('widgets/Spinner/Spinner',[
    'widgets/main',
    './SpinnerView',
    'widgets/utils/parserUtils'
], function (core, View, parserUtils) {
    

    /**
     * The Spinner class uses the Ericsson brand assets.
     *
     * @class Spinner
     */
    return core.Widget.extend({

        /*jshint validthis:true*/

        View: View,

        /**
         * The init method is automatically called by the constructor when using the "new" operator. If an object with
         * key/value pairs was passed into the constructor then the options variable will have those key/value pairs.
         * The following options are accepted:
         *   <ul>
         *       <li>value:     an integer used as a default value for the Spinner. Default is min value or 0.</li>
         *       <li>min:       an integer used as a min value for the Spinner. Default is 0.</li>
         *       <li>max:       an integer used as a max value for the Spinner.</li>
         *       <li>prefix:    a string used as a label for the Spinner.</li>
         *       <li>postfix:   a string used as a value type for the Spinner.</li>
         *       <li>inputSize: a string used to define size for the Spinner input field.</li>
         *   </ul>
         *
         * @method init
         * @param {Object} options
         */
        init: function (options) {
            this.options = options || {};

            this.minDelay = 100;
            this.maxDelay = 400;
            this.delayStep = 50;
        },

        /**
         * Overrides method from widget.
         * Executes every time, when added back to the screen.
         *
         * @method onViewReady
         * @private
         */
        onViewReady: function () {
            // TODO: not yet in jsCore. Will be removed in the future.
            this.view.afterRender();

            // Always start with minimal value
            if (this.options.min === undefined) {
                this.options.min = 0;
            }
            // If not defined default value, start with minimum value
            if (this.options.value === undefined) {
                this.options.value = this.options.min;
            }

            this.setValue(this.options.value);
            this.setPrefix(this.options.prefix || '');
            this.setPostfix(this.options.postfix || '');
            this.setInputSize(this.options.inputSize || 'miniW');

            this.view.getUpButton().addEventHandler('mousedown', startAutoIncrement, this);
            this.view.getUpButton().addEventHandler('mouseup', endAutoIncrement, this);
            this.view.getUpButton().addEventHandler('mouseout', endAutoIncrement, this);
            this.view.getDownButton().addEventHandler('mousedown', startAutoDecrement, this);
            this.view.getDownButton().addEventHandler('mouseup', endAutoDecrement, this);
            this.view.getDownButton().addEventHandler('mouseout', endAutoIncrement, this);

            this.view.getInput().addEventHandler('keypress', handleKeyPress, this);
            this.view.getInput().addEventHandler('keyup', handleKeyUp, this);
        },

        /**
         * Sets the text that appears in front of Spinner input
         *
         * @method setPrefix
         * @param {String} text Text in front of Spinner input
         */
        setPrefix: function (text) {
            if (text && text.trim() !== '') {
                var prefix = this.view.getPrefix();
                prefix.setStyle('display', 'table-cell');
                prefix.setText(text);
            }
        },

        /**
         * Sets the text that appears in behind of Spinner input
         *
         * @method setPostfix
         * @param {String} text Text behind Spinner input
         */
        setPostfix: function (text) {
            if (text && text.trim() !== '') {
                var postfix = this.view.getPostfix();
                postfix.setStyle('display', 'table-cell');
                postfix.setText(text);
            }
        },

        /**
         * Sets the value that will be visible in Spinner
         *
         * @method setValue
         * @param {int|String} value Integer or String containing Integer
         */
        setValue: function (value) {
            if (value !== undefined && value !== null) {
                if (value instanceof String || typeof(value) === 'string') {
                    var num = parserUtils.parseInt(value);
                    if (!isNaN(num)) {
                        this.value = getInRange.call(this, num);
                        update.call(this);
                    }
                } else if (/[0-9]|-/.test(value)) {
                    this.value = getInRange.call(this, value);
                    update.call(this);
                }
            }
        },

        /**
         * Returns the value that is presented in Spinner input field
         *
         * @method getValue
         * @return {int}
         */
        getValue: function () {
            return this.value;
        },

        /**
         * A method which allows to define input field size
         *
         * @param {String} inputSize Can be selected from available sizes: ['miniW', 'smallW', 'longW', 'xLongW']
         */
        setInputSize: function (inputSize) {
            this.view.getEbInput().setModifier(inputSize);
        }

    });

    /* ++++++++++++++++++++++++++++++++++++++++++ PRIVATE METHODS ++++++++++++++++++++++++++++++++++++++++++ */

    function inRange(value) {
        var min = this.options.min;
        var max = this.options.max;

        if (max === undefined) {
            return value >= min;
        } else {
            return value <= max && value >= min;
        }
    }

    function getInRange(value) {
        value = value > this.options.max ? this.options.max : value;
        return value < this.options.min ? this.options.min : value;
    }

    function handleKeyPress(event) {
        var key = String.fromCharCode(event.which);
        if(event.which < 47  || (event.which >= 112 && event.which <= 123)|| event.ctrlKey) {
            // Since Firefox is very restrictive on these keys, we have to manually allow for special keys like backspace
            // Here is good reading about madness about keycodes http://unixpapa.com/js/key.html
            // + more info on keycodes http://www.webonweboff.com/tips/js/event_key_codes.aspx
            return false;
        }

        // Regular expression to test if provided value is integer (can be also negative)
        var isInteger = /-?[0-9]/;
        return isInteger.test(key);
    }

    function handleKeyUp(event) {
        var value = parserUtils.parseInt(this.view.getInput().getValue());

        if (!inRange.call(this, value)) {
            // Roll back value to old one
            update.call(this);
        } else {
            this.value = value;
            applyModifiers.call(this);
        }
    }

    function autoIncrement() {
        clearTimeout(this.autoIncrementTimeout);
        this.autoIncrementTimeout = undefined;

        if (increment.call(this)) {
            if (this.autoIncrementDelay >= this.minDelay) {
                this.autoIncrementDelay -= this.delayStep;
            }

            this.autoIncrementTimeout = setTimeout(
                autoIncrement.bind(this),
                this.autoIncrementDelay
            );
        }
    }

    function startAutoIncrement() {
        this.autoIncrementDelay = this.maxDelay;
        this.autoIncrementTimeout = setTimeout(
            autoIncrement.bind(this),
            this.autoIncrementDelay
        );
    }

    function endAutoIncrement() {
        if (this.autoIncrementTimeout) {
            clearTimeout(this.autoIncrementTimeout);
            increment.call(this);
            this.autoIncrementTimeout = undefined;
        }
    }

    function increment() {
        if (inRange.call(this, this.value + 1)) {
            this.value++;
            update.call(this);
            return true;
        }
        return false;
    }

    function autoDecrement() {
        clearTimeout(this.autoDecrementTimeout);
        this.autoDecrementTimeout = undefined;

        if (decrement.call(this)) {
            if (this.autoDecrementDelay >= this.minDelay) {
                this.autoDecrementDelay -= this.delayStep;
            }

            this.autoDecrementTimeout = setTimeout(
                autoDecrement.bind(this),
                this.autoDecrementDelay
            );
        }
    }

    function startAutoDecrement() {
        this.autoDecrementDelay = this.maxDelay;
        this.autoDecrementTimeout = setTimeout(
            autoDecrement.bind(this),
            this.autoDecrementDelay
        );
    }

    function endAutoDecrement() {
        if (this.autoDecrementTimeout) {
            clearTimeout(this.autoDecrementTimeout);
            decrement.call(this);
            this.autoDecrementTimeout = undefined;
        }
    }

    function decrement() {
        if (inRange.call(this, this.value - 1)) {
            this.value--;
            update.call(this);
            return true;
        }
        return false;
    }

    function update() {
        // Set input value
        this.view.getInput().setValue(this.value);

        // Enable/Disable up/down buttons
        applyModifiers.call(this);
    }

    function applyModifiers() {
        // Up button
        if (inRange.call(this, this.value + 1)) {
            this.view.getUpButton().setAttribute('disabled', false);
        } else {
            this.view.getUpButton().setAttribute('disabled', true);
        }

        // Down button
        if (inRange.call(this, this.value - 1)) {
            this.view.getDownButton().setAttribute('disabled', false);
        } else {
            this.view.getDownButton().setAttribute('disabled', true);
        }
    }
});
define('widgets/Spinner', ['widgets/Spinner/Spinner'], function (main) { return main; });

/*global define*/
define('widgets/TimePicker/TimePicker',[
    'widgets/main',
    './TimePickerView',
    'widgets/Spinner'
], function (core, View, Spinner) {
    

    /**
     * The TimePicker class uses the Ericsson brand assets.
     *
     * @class TimePicker
     */
    return core.Widget.extend({

        View: View,

        /**
         * The init method is automatically called by the constructor when using the "new" operator. If an object with
         * key/value pairs was passed into the constructor then the options variable will have those key/value pairs.
         *
         * The following options are accepted:
         *   <ul>
         *       <li>hours: an integer used as a defined hours for TimePicker. Default is 0.</li>
         *       <li>minutes: an integer used as a defined minutes for TimePicker. Default is 0.</li>
         *       <li>seconds: an integer used as a defined seconds for TimePicker. Default is 0.</li>
         *   </ul>
         *
         * @method init
         * @param {Object} options
         */
        init: function (options) {
            options = options || {};

            this.hoursSpinner = new Spinner({
                value: options.hours || 0,
                min: 0,
                max: 23,
                postfix: 'h'
            });

            this.minutesSpinner = new Spinner({
                value: options.minutes || 0,
                min: 0,
                max: 59,
                postfix: 'm'
            });

            this.secondsSpinner = new Spinner({
                value: options.seconds || 0,
                min: 0,
                max: 59,
                postfix: 's'
            });
        },

        /**
         * Overrides method from widget.
         * Executes every time, when added back to the screen.
         *
         * @method onViewReady
         * @private
         */
        onViewReady: function () {
            this.getElement().append(this.hoursSpinner.getElement());
            this.getElement().append(this.minutesSpinner.getElement());
            this.getElement().append(this.secondsSpinner.getElement());
        },

        /**
         * Sets value to the TimePicker
         *
         * @method setValue
         * @param {int} hours
         * @param {int} minutes
         * @param {int} seconds
         */
        setValue: function (hours, minutes, seconds) {
            this.hoursSpinner.setValue(hours);
            this.minutesSpinner.setValue(minutes);
            this.secondsSpinner.setValue(seconds);
        },

        /**
         * Returns the value that is presented in the TimePicker
         *
         * @method getValue
         * @return {object} Object representing time in the TimePicker {hours: int, minutes: int, seconds: int}
         */
        getValue: function () {
            return {
                hours: this.hoursSpinner.getValue(),
                minutes: this.minutesSpinner.getValue(),
                seconds: this.secondsSpinner.getValue()
            };
        }

    });

});

define('widgets/TimePicker', ['widgets/TimePicker/TimePicker'], function (main) { return main; });

/*global define*/
define('widgets/DateTimePicker/DateTimePicker',[
    'widgets/main',
    './DateTimePickerView',
    'widgets/DatePicker',
    'widgets/TimePicker'
], function (core, View, DatePicker, TimePicker) {
    

    /**
     * The DateTimePicker class uses the Ericsson brand assets.
     *
     * @class DateTimePicker
     */
    return core.Widget.extend({
        /*jshint validthis:true*/

        View: View,

        /**
         * The init method is automatically called by the constructor when using the "new" operator. If an object with
         * key/value pairs was passed into the constructor then the options variable will have those key/value pairs.
         *
         * The following options are accepted:
         *   <ul>
         *       <li>value: a date used as initial date and time for DateTimePicker.</li>
         *       <li>dateInRange: a function used as date range for DateTimePicker. By default date range not defined.</li>
         *       <li>disableDay: a function used as a disabled day for DateTimePicker. By default a disabled day not defined.</li>
         *       <li>defaultDate: a function used as default date to show when just opening DateTimePicker.</li>
         *   </ul>
         *
         * @method init
         * @param {Object} options
         */
        init: function (options) {
            this.options = options || {};

            var dateTime = this.options.value;
            this.datePicker = new DatePicker({
                value: dateTime,
                dateInRange: this.options.dateInRange,
                disableDay: this.options.disableDay,
                defaultDate: this.options.defaultDate
            });

            var timeObj = getDefaultTime.call(this, dateTime);

            this.timePicker = new TimePicker(timeObj);
        },

        /**
         * Overrides method from widget.
         * Executes every time, when added back to the screen.
         *
         * @method onViewReady
         * @private
         */
        onViewReady: function () {
            this.view.getDateHolder().append(this.datePicker.getElement());
            this.view.getTimeHolder().append(this.timePicker.getElement());
        },

        /**
         * Sets date in the DateTimePicker.
         *
         * @method setValue
         * @param {Date} date Date object to represent selected day in the DateTimePicker
         */
        setValue: function (date) {
            this.datePicker.setValue(date);

            var timeObj = getDefaultTime.call(this, date);

            this.timePicker.setValue(timeObj.hours, timeObj.minutes, timeObj.seconds);
        },

        /**
         * Returns selected date from the DateTimePicker.
         *
         * @method getValue
         * @return {Date} date Date representing value in the DateTimePicker or null if not selected.
         */
        getValue: function () {
            var date = this.datePicker.getValue();
            if (date === null) {
                return date;
            }

            var time = this.timePicker.getValue();
            date.setHours(time.hours);
            date.setMinutes(time.minutes);
            date.setSeconds(time.seconds);

            return date;
        }

    });

    function getDefaultTime(date) {
        var timeObj = {};
        if (date instanceof Date) {
            timeObj.hours = date.getHours();
            timeObj.minutes = date.getMinutes();
            timeObj.seconds = date.getSeconds();
        } else {
            var defaultDate;
            if (this.options.defaultDate && this.options.defaultDate instanceof Date) {
                defaultDate = this.options.defaultDate;
            } else if (this.options.defaultDate && this.options.defaultDate instanceof Function) {
                defaultDate = this.options.defaultDate();
            } else {
                defaultDate = new Date();

                // Minutes & seconds by default are 0
                defaultDate.setMinutes(0);
                defaultDate.setSeconds(0);
                defaultDate.setMilliseconds(0);
            }

            timeObj.hours = defaultDate.getHours();
            timeObj.minutes = defaultDate.getMinutes();
            timeObj.seconds = defaultDate.getSeconds();
        }

        return timeObj;
    }

});

define('widgets/DateTimePicker', ['widgets/DateTimePicker/DateTimePicker'], function (main) { return main; });
