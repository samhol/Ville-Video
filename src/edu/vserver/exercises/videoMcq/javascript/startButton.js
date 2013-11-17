/**
 * @fileOverview VideoMcqExecutor's start button handling
 * @author Sami Petteri Holck <sami.holck@gmail.com>
 * @license MIT License
 * @license GPL v2
 */

/**
 * Contains all start button modification functionality.
 *
 * @author Sami Petteri Holck <sami.holck@gmail.com>
 * @name VideoMcqExecutor
 * @namespace
 */
(function (VideoMcqExecutor, $, undefined) {
	"use strict";


	/**
	 * Sets some initial properties and repositions the start button.
	 *
	 * @public
	 * @static
	 * @memberOf VideoMcqExecutor
	 */
	VideoMcqExecutor.setStartButton = function () {
		console.log("VideoMcqExecutor.setStartButton()");
		var startButton = $(".v-button-ExerStarter");
		startButton.css({
			'z-index': 300
		});
		VideoMcqExecutor.repositionStartButton();
	};


	/**
	 * Repositions the start button.
	 *
	 * @public
	 * @static
	 * @memberOf VideoMcqExecutor
	 */
	VideoMcqExecutor.repositionStartButton = function () {
		console.log("VideoMcqExecutor.repositionStartButton()");
		var startButton = $(".v-button-ExerStarter");
		startButton.position({
			my: "center top-64",
			at: "center",
			of: ".video-js"
		});
	};

	/**
	 * Shows the start button.
	 *
	 * @public
	 * @static
	 * @memberOf VideoMcqExecutor
	 */
	VideoMcqExecutor.showStartButton = function () {
		console.log("\t\tVideoMcqExecutor.showStartButton()");
		$(".v-button-ExerStarter").show();
	};

	/**
	 * Hides the start button.
	 *
	 * @public
	 * @static
	 * @memberOf VideoMcqExecutor
	 */
	VideoMcqExecutor.hideStartButton = function () {
		console.log("VideoMcqExecutor.hideStartButton()");
		$(".v-button-ExerStarter").hide();
	};
}(window.VideoMcqExecutor = window.VideoMcqExecutor || {}, jQuery));


$(window).resize(function () {
	"use strict";
	console.log("window.resize");
	VideoMcqExecutor.repositionStartButton();
});