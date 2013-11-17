/**
 * @file Contains all the JavaScript functionality of the SvgDots clien side JavaScript application.
 * @copyright Sami Holck
 * @author    Sami Petteri Holck <sami.holck@gmail.com>
 * @version   1.01
 */

/**
 * Raphael JavaScript library for SVG vector graphics.
 * @external Raphael
 * @version  2.1.2
 * @see      {@link http://raphaeljs.com/ Raphael}
 */

/**
 * FRaphael extension for Raphael.js to make it easier to work with Filter Effects.
 * FRaphael is delivered with and licensed under the MIT license.
 * @external FRaphael
 * @author   Chris Scott <chris.scott@factmint.com>
 * @version  0.0.1
 * @see      {@link https://github.com/chrismichaelscott/fraphael/ FRaphael}
 */

/**
 * The built in DOMElement object.
 * @external DOMElement
 * @alias    bar
 * @see      {@link https://developer.mozilla.org/en-US/docs/Web/API/element DOMElement}
 */

/**
 * @namespace
 * @name sphSvg
 * @description Contains some SVG drawing tools. Uses {@link external:Raphael Raphael}
 * JavaScript library.
 * @author Sami Petteri Holck <sami.holck@gmail.com>
 * @author Sami Petteri Holck
 */
(function (sphSvg, undefined) {
	"use strict";

	/**
	 * Constructs a new SvgDots object.
	 *
	 * @author    Sami Petteri Holck <sami.holck@gmail.com>
	 * @class
	 * @constructor
	 * @memberOf  sphSvg
	 * @classdesc SvgDots draws an SVG image containing small dots using {@link external:Raphael| Raphael} JavaScript library.
	 * @param     {external:DOMElement} target the target DOMElement Object where the SVG component is added.
	 * @returns   {sphSvg.SvgDots} the SvgDots object.
	 */
	sphSvg.SvgDots = function (target) {
		console.log("sphSvg.SvgDots(target: " + target + ")");
		this.target = target;
		this.circles = [];
		this.paper = new Raphael(target, 700, 20);
		this.typeOf = "sphSvg.SvgDots";
		return this;
	};

	sphSvg.SvgDots.prototype = {

		/**
		 * @typedef  DotData
		 * @type     {Object}
		 * @property {boolean} clickable - the clickable state of the dot.
		 * @property {string} color - the color of the dot.
		 * @property {string} title - the title of the dot.
		 */

		/**
		 * Draws the Dots to the SVG canvas.
		 *
		 * @public
		 * @memberOf sphSvg.SvgDots#
		 * @param    {Array.<DotData>} dots an Array of {@link DotData} objects.
		 * @returns  {sphSvg.SvgDots} the SvgDots object.
		 * @see      {@link external:Raphael Raphael}
		 * @see      {@link external:FRaphael FRaphael}
		 */
		draw : function (dots) {
			console.log("sphSvg.SvgDots.draw(dots: " + dots + ")");
			this.dots = dots;
			this.paper.clear();
			var index = 0, y = 11, x = 0, rows = 1;
			for (index; index < this.dots.length; index++) {
				if (index !== 0 && (index % 38) === 0) {
					y = y + 17;
					x = 0;
					rows += 1;
					this.paper.setSize(700, rows * 20);
				}
				x += 18;
				console.log("dot: " + this.dots[index].color);
				this.circles[index] = this.paper.circle(x, y, 6);
				this.setDotTitle(index, this.dots[index].title);
				this.circles[index].attr({fill: this.dots[index].color, stroke: "black", "stroke-width": 2, title: this.dots[index].title})
					.emboss()
					.shadow(2, 2, 2, 0.5, this.dots[index].color);
				this.circles[index].data("index", index);
				this.setDotClickable(index, this.dots[index].clickable);
			}
			return this;
		},

		/**
		 * Replaces the dot located on the given index.
		 *
		 * @public
		 * @memberOf sphSvg.SvgDots#
		 * @param    {Number} index the index of the target dot.
		 * @param    {DotData} dot required dot data.
		 * @returns  {sphSvg.SvgDots} the SvgDots object.
		 * @see      {@link external:Raphael Raphael}
		 */
		replaceDot: function (index, dot) {
			console.log("sphSvg.SvgDots.repaintDot(index: " + index + ", dotData: " + dot + ")");
			this.circles[index].attr({fill: dot.color, title: dot.title});
			return this;
		},

		/**
		 * Changes the clickable state of the dot.
		 *
		 * @public
		 * @memberOf sphSvg.SvgDots#
		 * @param    {Boolean} clickable a boolean value specifying whether the dot is clickable or not.
		 * @returns  {sphSvg.SvgDots} the SvgDots object.
		 */
		setDotClickable: function (index, clickable) {
			var that = this, clicked = function () {
				that.callback("click", index);
			};
			if (clickable) {
				this.circles[index].click(clicked);
			} else {
				this.circles[index].unclick(clicked);
			}
		},

		/**
		 * Sets the color of the dot located on the given index.
		 *
		 * @public
		 * @memberOf sphSvg.SvgDots#
		 * @param    {Number} index the index of the target dot.
		 * @param    {String} color the new color of the dot.
		 * @returns  {sphSvg.SvgDots} the SvgDots object.
		 */
		setDotColor: function (index, color) {
			console.log("sphSvg.SvgDots.setDotColor(index: " + index + ", color: " + color + ")");
			this.circles[index].attr({fill: color});
			return this;
		},

		/**
		 * Sets the title of the dot located on the given index.
		 *
		 * @public
		 * @memberOf sphSvg.SvgDots#
		 * @param    {Number} index the index of the target dot.
		 * @param    {String} title the new title of the dot.
		 * @returns  {sphSvg.SvgDots} the SvgDots object.
		 */
		setDotTitle: function (index, title) {
			console.log("sphSvg.SvgDots.setDotTitle(index: " + index + ", title: " + title + ")");
			this.circles[index].attr({title: title});
			return this;
		},

		/**
		 * Callback method for delegating SVG dot events from a {@link sphSvg.SvgDots} object.
		 *
		 * @callback dotEventCallback
		 * @param    {String} event the name of the {@link external:Raphael Raphael} event.
		 * @param    {Number} index the index of the SVG dot that triggered the event.
		 */

		/**
		 * Sets the callback function for even delegation to other systems.
		 *
		 * @public
		 * @memberOf sphSvg.SvgDots#
		 * @param    {dotEventCallback} callback the event callback function.
		 * @returns  {sphSvg.SvgDots} the SvgDots object.
		 */
		setEventCallback: function (callback) {
			console.log("sphSvg.SvgDots.setEventCallback(callback: " + callback + ")");
			this.callback = callback;
			return this;
		}
	};

}(window.sphSvg = window.sphSvg || {}));

/**
 * @namespace
 * @name window
 * @description supplemental window methods.
 */

/**
 * RPC mechanism for communicating between client and server
 *
 * @author    Sami Petteri Holck <sami.holck@gmail.com>
 * @copyright Sami Holck
 * @namespace
 * @name     sph_vaadin_ui_svg_SvgDots
 * @memberOf window
 */
window.sph_vaadin_ui_svg_SvgDots = function () {
	"use strict";
	console.log("\n---------------------");
	console.log("sph_vaadin_ui_svg_SvgDots() {");
	var dotDrawer = new sphSvg.SvgDots(this.getElement()), dots = [];

	dotDrawer.setEventCallback(this.delegateDotEvent);

	/**
	 * Method runs when the Vaadin server attempts to update the client values.
	 *
	 * @function window.sph_vaadin_ui_svg_SvgDots~onStateChange
	 */
	this.onStateChange = function () {
		console.log("\tonStateChange() {\n");
		if (!_.isEqual(dots, this.getState().dots)) {
			console.log(dots);
			dots = this.getState().dots;
			dotDrawer.draw(dots);
		}
		console.log("\t}");
	};

	/**
	 * Replaces the dot located on the given index.
	 *
	 * @function window.sph_vaadin_ui_svg_SvgDots~replaceDot
	 * @param    {Number} index the index of the target dot.
	 * @param    {DotData} dot required dot data.
	 */
	this.replaceDot = function (index, dot) {
		dotDrawer.replaceDot(index, dot);
	};

	/**
	 * Sets the color of the dot located on the given index.
	 *
	 * @function window.sph_vaadin_ui_svg_SvgDots~setDotColor
	 * @param    {Number} index the index of the target dot.
	 * @param    {String} color the new color of the dot.
	 */
	this.setDotColor = function (index, color) {
		dotDrawer.setDotColor(index, color);
	};

	/**
	 * Sets the title of the dot located on the given index.
	 * @public
	 * @function window.sph_vaadin_ui_svg_SvgDots~setDotTitle
	 * @param    {Number} index the index of the target dot.
	 * @param    {String} title the new title of the dot.
	 */
	this.setDotTitle = function (index, title) {
		dotDrawer.setDotTitle(index, title);
	};

	/**
	 * Changes the clickable state of the dot.
	 * @public
	 * @function window.sph_vaadin_ui_svg_SvgDots~setDotClickable
	 * @param    {Number} index the index of the target dot.
	 * @param    {boolean} clickable a boolean value specifying whether the dot is clickable or not.
	 */
	this.setDotClickable = function (index, clickable) {
		dotDrawer.setDotTitle(index, clickable);
	};
	console.log("} sph_vaadin_ui_svg_SvgDots");
	console.log("---------------------\n");
	/**
	 * Callback method for delegating SVG dot events from a {@link sphSvg.SvgDots} object to the Vaadin server.
	 *
	 * @function window.sph_vaadin_ui_svg_SvgDots~delegateDotEvent
	 * @type     {dotEventCallback}
	 * @param    {String} eventName the name of the {@link external:Raphael Raphael} event.
	 * @param    {Number} index the index of the SVG dot that triggered the event.
	 */
};