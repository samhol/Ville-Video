/**
 * @file Contains all the JavaScript functionality of the Videojs application
 *
 * @author Sami Petteri Holck <sami.holck@gmail.com>
 * @requires jQuery 1.10.2
 * @requires Video.js 4.1.0
 * @requires Underscore.js 1.5.1
 * @version 2.0.3
 */

/**
 * The open source HTML5 video player
 * @external videojs
 * @version 4.1.0
 * @see {@link http://www.videojs.com|Video.js}
 *
 */

/**
 * Video.js play event
 *
 * @event external:videojs#play
 * @type {external:videojs}
 */

/**
 * Video.js pause event
 *
 * @event external:videojs#pause
 * @type {external:videojs}
 */

/**
 * Video.js timeupdate event
 *
 * @event external:videojs#timeupdate
 * @type {external:videojs}
 */

/**
 * Video.js resize event
 *
 * @event external:videojs#resize
 * @type {external:videojs}
 */

/**
 * jQuery JavaScript Library.
 * @external jQuery
 * @version 1.10.2
 * @see {@link http://raphaeljs.com/ Raphael}
 *
 * @exports $ as jQuery
 */

/**
 * The jQuery plugin namespace.
 * @external fn
 * @memberOf external:jQuery
 * @version 1.10.2
 * @see {@link http://learn.jquery.com/plugins/ The jQuery Plugin Guide}
 */

/**
 * Underscore JavaScript Library which provides functions for various tasks.
 * @external _
 * @version 1.5.2
 * @see {@link http://underscorejs.org|Underscore.js}
 */

/**
 * Contains all videoMcq functionality.
 *
 * @author Sami Petteri Holck <sami.holck@gmail.com>
 * @name videoMcq
 * @namespace
 */
(function (videoMcq, $, undefined) {
	"use strict";

	/**
	 * Constructs a new VideoPlayer to control the {@link external:videojs | videojs} player.
	 *
	 * @memberOf videoMcq
	 * @class
	 * @classdesc VideoPlayer is a simple wrapper class for {@link external:videojs | videojs} player.
	 * @constructor
	 * @param {String} vjsId the video id
	 * @param {String} src the source of the video
	 * @param {String} mime the MIME type of the video
	 * @returns {videoMcq.VideoPlayer} the player object.
	 */
	videoMcq.VideoPlayer = function (vjsId, src, mime) {
		console.log("videoMcq.VideoPlayer(vjsId: " + vjsId + ", src: " + src + ", mime: " + mime + ")");
		var that = this;
		this.vjsId = vjsId;
		this.src = src;
		this.mime = mime;
		this.initVideojs();
		this.typeOf = "videoMcq.VideoPlayer";
		this.pauseTimes = [];
		this.nextPause = Number.MAX_VALUE;
		this.masked = false;
		this.supportsFullScreen = true;
		this.player.ready(function () {
			that.player.on("play", $.proxy(that.onPlayOrPause, that));
			that.player.on("pause", $.proxy(that.onPlayOrPause, that));
			that.player.on("timeupdate", $.proxy(that.onTimeupdate, that));
		});
		return this;
	};



	videoMcq.VideoPlayer.prototype = {

		/**
		 * Creates the {@link external:videojs | videojs} object from the video tag pointed by the
		 * given domId.
		 *
		 * @private
		 * @memberOf videoMcq.VideoPlayer#
		 * @returns {videoMcq.VideoPlayer} the player object.
		 */
		initVideojs : function () {
			console.log("VideoPlayer.initVideojs()");
			videojs.options.flash.swf = "http://localhost/VaadiProject/VAADIN/themes/ville-theme/exercises/video-js.swf";
			if (this.src !== "") {
				if (this.mime === "youtube" || this.mime === "vimeo" || this.mime === "dailymotion") {
					this.player = videojs(this.vjsId, {
						'techOrder' : [ this.mime ],
						'src' : this.src,
						'loop' : false,
						'autoplay' : false,
						'preload': 'auto'
					});
				} else {
					this.player = videojs(this.vjsId, {}).src({
						'type' : this.mime,
						'src' : this.src,
						'loop' : false,
						'autoplay' : false,
						'preload': 'auto'
					});
				}
			} else {
				this.player = videojs(this.vjsId, {
					controls : false
				});
			}
			this.vjs = $("#" + this.vjsId);
			return this;
		},

		/**
		 * Sets the next pause time.
		 *
		 * @private
		 * @memberOf videoMcq.VideoPlayer#
		 * @returns  {videoMcq.VideoPlayer} the player object.
		 */
		setNextPause : function () {
			var that = this;
			//console.log("\t\tVideoPlayer.setNextPause()");
			this.nextPause = _.find(this.pauseTimes, function (num) {
				return num > that.player.currentTime();
			});
			if (isNaN(this.nextPause)) {
				this.nextPause = Number.MAX_VALUE;
				//console.log("\t\t\tisNaN(this.nextPause) => this.nextPause = " + this.nextPause);
			}
			//console.log("\t\t\tthis.nextPause = " + this.nextPause + ", pauseAts = " + this.pauseTimes);
			return this;
		},
		/**
		 * VideoPlayer's common functionality for {@link external:videojs#event:play play}
		 * and {@link external:videojs#event:pause pause} events.
		 *
		 * @private
		 * @memberOf videoMcq.VideoPlayer#
		 */
		onPlayOrPause : function () {
			//console.log("VideoPlayer.onPlayOrPause()");
			this.setNextPause();
		},

		/**
		 * VideoPlayer's {@link external:videojs#event:timeupdate | timeupdate event} event functionality.
		 *
		 * @private
		 * @memberOf videoMcq.VideoPlayer#
		 */
		onTimeupdate : function () {
			//console.log("\t\tVideoPlayer.onTimeupdate()");
			if (this.player.currentTime() >= this.nextPause) {
				//console.log("\t\tVideoPlayer.onTimeupdate()");
				//console.log("\t\t\tpauseAt happened:");
				//console.log("\t\t\t\tthis.player.currentTime() = " + this.player.currentTime());
				//console.log("\t\t\t\tthis.nextPause = " + this.nextPause);
				this.player.pause();
				this.setNextPause();
			}
		},

		/**
		 * Disposes the inner {@link external:videojs videojs} player
		 *
		 * @memberOf videoMcq.VideoPlayer#
		 * @returns {videoMcq.VideoPlayer} the player object.
		 */
		dispose : function () {
			console.log("videoMcq.dispose()");
			this.player.dispose();
			return this;
		},

		/**
		 * Returns the {@link external:videojs videojs} player object.
		 *
		 * @public
		 * @memberOf videoMcq.VideoPlayer#
		 * @returns  {external:videojs} the {@link external:videojs videojs} player object.
		 */
		getVideojs: function () {
			console.log("\t\tVideoPlayer.getVideojs()");
			return this.player;
		},

		/**
		 * Plays the video.
		 *
		 * @public
		 * @memberOf videoMcq.VideoPlayer#
		 * @returns  {videoMcq.VideoPlayer} the player object.
		 * @fires    external:videojs#play
		 */
		play: function () {
			console.log("VideoPlayer.play()");
			this.player.ready(function () {
				if (this.paused()) {
					console.log("\t\tvideoMcq.play()");
					this.play();
				}
			});
			return this;
		},

		/**
		 * Pauses the video.
		 *
		 * @public
		 * @memberOf videoMcq.VideoPlayer#
		 * @returns  {videoMcq.VideoPlayer} the player object.
		 * @fires    external:videojs#pause
		 */
		pause: function () {
			console.log("VideoPlayer.pause()");
			this.player.ready(function () {
				if (!this.paused()) {
					console.log("\t\tVideoPlayer.play()");
					this.paused();
				}
			});
			return this;
		},

		/**
		 * Seeks to the given time in the video stream and pauses the playback.
		 *
		 * @public
		 * @memberOf videoMcq.VideoPlayer#
		 * @param   {Number} seekTo the time
		 * @returns {videoMcq.VideoPlayer} the player object.
		 */
		seekTo: function (seekTo) {
			console.log("\t\tVideoPlayer.seekTo(" + seekTo + ")");
			this.player.ready(function () {
				this.currentTime(seekTo).pause();
			});
			return this;
		},

		/**
		 * Pauses the video playback at given time(s). Note: Uses the {@link external:_ Undescore.js}.
		 *
		 * @public
		 * @memberOf videoMcq.VideoPlayer#
		 * @param    {Array<number>} pauseTimes a collection of times in seconds where the video stream is paused.
		 * @returns  {videoMcq.VideoPlayer} the player object.
		 */
		pauseAt : function (pauseTimes) {
			console.log("\t\tVideoPlayer.pauseAt(" + pauseTimes + ")");
			if (!_.isEqual(pauseTimes, this.pauseTimes)) {
				this.pauseTimes = pauseTimes;
			}
			return this;
		},

		/**
		 * Changes the width and height of the Video.js player to the supplied width and height.
		 *
		 * @public
		 * @memberOf videoMcq.VideoPlayer#
		 * @param    {Number} width The width in pixels
		 * @param    {Number} height The height in pixels
		 * @returns  {videoMcq.VideoPlayer} the player object.
		 * @fires    external:videojs#resize
		 */
		setDimensions : function (width, height) {
			console.log("\t\tVideoPlayer.setDimensions(width: " + width + "px, height: " + height + "px)");
			this.player.ready(function () {
				this.dimensions(width, height);
			});
			return this;
		},

		/**
		 * Specifies whether this {@link external:videojs videojs} player is set to full window mode or not.
		 *
		 * @public
		 * @memberOf videoMcq.VideoPlayer#
		 * @param    {boolean} fullWindow whether this {@link external:videojs videojs} player is set to full window mode.
		 * @returns  {videoMcq.VideoPlayer} the player object.
		 * @fires    external:videojs#resize
		 */
		setFullWindow : function (fullWindow) {
			console.log("\t\tVideoPlayer.setFullWindow()");
			var button, that = this, closeFunction;
			if (fullWindow) {
				this.width = this.player.width();
				this.height = this.player.height();
				this.vjs.css({
					position: 'fixed',
					width: "",
					height: "",
					top: '0px',
					right: '0px',
					bottom: '0px',
					left: '0px',
					'z-index': 10
				});
				button = $('<div class="centerer"><div class="exitFullWindow">Exit full window mode</div></div>');
				button.appendTo(this.vjs);
				closeFunction = function (event) {
					console.log("closeFunction: " + (!event.data.isFull));
					that.setFullWindow(!event.data.isFull);
				};
				this.vjs.on("click", ".exitFullWindow", {'isFull': fullWindow}, closeFunction);
				this.vjs.on('dblclick', ".loadmask", {'isFull': fullWindow}, closeFunction);
			} else {
				this.vjs.find("div.centerer").remove();
				this.vjs.css({
					position: "",
					top: "",
					right: "",
					bottom: "",
					left: "",
					'z-index': ""
				});
				if (this.width && this.height) {
					this.player.width(this.width);
					this.player.height(this.height);
				}
			}
			return this;
		},

		/**
		 * Specifies whether the {@link external:videojs videojs} player supports full screen mode.
		 *
		 * @public
		 * @memberOf videoMcq.VideoPlayer#
		 * @param    {boolean} enabled whether the {@link external:videojs videojs} player supports full screen mode.
		 * @returns  {videoMcq.VideoPlayer} the player object.
		 */
		supportFullscreenMode: function (enabled) {
			console.log("\t\tVideoPlayer.supportFullscreenMode(enabled: " + enabled + ")");
			this.fullscreenSupport = enabled;
			if (!enabled) {
				this.vjs.find(".vjs-fullscreen-control").css("visibility", "hidden");
				if (!this.vjs.isMasked()) {
					this.vjs.mask();
				}
			} else {
				this.vjs.find(".vjs-fullscreen-control").css("visibility", "visible");
				this.vjs.unmask();
			}
			return this;
		},

		/**
		 * Specifies whether this {@link Videojs} player has minimal user interface controls.
		 *
		 * NOTES:
		 *
		 * Minimal user interface controls are all on player's controls panel:
		 *
		 * * Play/Pause button
		 * * Volume slider
		 * * Volume muting button
		 *
		 * ...and hidden controls are:
		 *
		 * * Big play button
		 * * Progress bar with all of its functionality
		 *
		 * @public
		 * @memberOf videoMcq.VideoPlayer#
		 * @param    {boolean} enabled
		 * @returns  {videoMcq.VideoPlayer}
		 */
		minimalUserControlsEnabled: function (enabled) {
			console.log("\t\tVideoPlayer.minimalUserControlsEnabled(enabled: " + enabled + ")");
			if (enabled) {
				this.vjs.find(".vjs-big-play-button").hide();//.css("visibility", "hidden");
				this.vjs.find(".vjs-control-bar").css("z-index", 500);
				this.vjs.find(".vjs-progress-control").css("visibility", "hidden");
			} else {
				this.vjs.find(".vjs-big-play-button").show();//.css("visibility", "");
				this.vjs.find(".vjs-control-bar").css("z-index", "");
				this.vjs.find(".vjs-progress-control").css("visibility", "");
			}
			return this;
		},

		/**
		 * Displays a mask over the entire Video.js player element.
		 *
		 * @public
		 * @memberOf videoMcq.VideoPlayer#
		 * @param    {String} [label=null] Text message that will be displayed on top of the mask (optional).
		 * @param    {Number} [delay=0] Delay in milliseconds before player element is masked (optional).
		 * @returns  {videoMcq.VideoPlayer} the player object.
		 */
		mask: function (label, delay) {
			console.log("\t\tVideoPlayer.mask(label: " + label + ", delay:" + delay + ")");
			this.vjs.mask(label, delay);
			this.vjs.find(".vjs-control-bar").css("z-index", "");
			return this;
		},

		/**
		 * Removes the mask from the Video.js player element.
		 *
		 * @public
		 * @memberOf videoMcq.VideoPlayer#
		 * @returns  {videoMcq.VideoPlayer} the player object.
		 */
		unmask: function () {
			console.log("\t\tVideoPlayer.unmask()");
			this.vjs.find(".vjs-control-bar").css("z-index", 500);
			if (this.fullscreenSupport) {
				this.vjs.unmask();
			}
			return this;
		},

		/**
		 * Sets the {@link external:videojs} event callback function for the client.
		 *
		 * @public
		 * @memberOf videoMcq.VideoPlayer#
		 * @param   {Function} eventCallback callback function
		 */
		setEventCallback: function (eventCallback) {
			console.log("\t\tVideoPlayer.setEventCallback(eventCallback: " + eventCallback + ")");
			this.eventCallback = eventCallback;
			this.setListeners();
		},

		/**
		 * Sets the Video.js event listeners for the server.
		 *
		 * @private
		 * @memberOf videoMcq.VideoPlayer#
		 */
		setListeners: function () {
			console.log("\t\tVideoPlayer.setListeners()");
			console.log("setListeners()");
			var eventCallback_ = this.eventCallback, videoDuration = 0;
			this.player.ready(function () {
				var player = this;

				/**
				 * Delegates the events to the corresponding callback function
				 *
				 * @private
				 * @memberOf setListeners
				 * @param    {Event} event delegated event
				 */
				function delegate(event) {
					var eventName = event.type,
						currentTime = player.currentTime();
					if (player.duration() > 0 && player.duration() !== videoDuration) {
						videoDuration = player.duration();
						eventCallback_("durationchange", currentTime, player.duration());
					} else {
						eventCallback_(eventName, currentTime);
					}
				}
				player.on("loadstart", delegate);
				player.on("loadedmetadata", delegate);
				player.on("loadeddata", delegate);
				player.on("loadedalldata", delegate);
				player.on("play", delegate);
				player.on("pause", delegate);
				player.on("timeupdate", delegate);
				player.on("ended", delegate);
				player.on("durationchange", delegate);
				player.on("progress", delegate);
				player.on("resize", delegate);
				player.on("volumechange", delegate);
				player.on("error", delegate);
				player.on("fullscreenchange", delegate);
			});
		}
	};

	/**
	 * @typedef  VideojsPlayerData
	 * @type     {Object}
	 * @property {String} videoJsId - the DOM id of the Video.js player.
	 * @property {String} videoSrc - the URL of the video stream.
	 * @property {String} mimetype - the MIME type of the video stream.
	 * @property {?string} poster - the poster of the player.
	 * @property {Number} videoWidth - the width of the player in pixels.
	 * @property {Number} videoHeight - the height of the player in pixels.
	 */

	/**
	 * Creates the video tag to the VideojsPlayer widget component.
	 *
	 * @public
	 * @static
	 * @memberOf videoMcq
	 * @param    {external:jQuery} widget the widget component.
	 * @param    {VideojsPlayerData} serverData state objects content.
	 * @returns  {videoMcq.VideoPlayer} an instance of VideoPlayer
	 */
	videoMcq.init = function (widget, serverData) {
		var videoJsId, tagAttrs;
		videojs.options.flash.swf = "http://s.ytimg.com/yts/swfbin/player-vflNa3-a8/ad3.swf";
		videoJsId = serverData.videoJsId;
		console.log("videoMcq.init(widget: " + widget + ", serverData: " + serverData + ") {");
		tagAttrs = {
			id : videoJsId,
			'class' : "video-js vjs-default-skin",
			controls : '',
			poster : (serverData.poster) ? serverData.poster : null,
			text : 'Your browser does not support the video tag!'
		};
		// Creates the video tag to the VideojsPlayer's widget frame
		$('<video/>', tagAttrs)
			.attr("width", serverData.vjsWidth)
			.attr("height", serverData.vjsHeight).appendTo(widget);
		console.log("\ttag: '" + widget.clone().html() + "' inserted to the widget");
		console.log("}");
		return new videoMcq.VideoPlayer(videoJsId, serverData.videoSrc, serverData.mimetype);
	};
}(window.videoMcq = window.videoMcq || {}, jQuery));

/**
 * @namespace
 * @name window
 * @description supplemental window methods.
 */

/**
 * This namespace is used as an RPC mechanism for communicating between  the
 * {@link external:videojs videojs} player client object and Java's Videojs object on Vaadin server.
 *
 * @namespace
 * @memberOf  window
 * @name      sph_vaadin_ui_videojs_Videojs
 * @author    Sami Holck <sami.holck@gmail.com>
 * @copyright Sami Holck 2013
 */
window.sph_vaadin_ui_videojs_Videojs = function () {
	"use strict";
	var videoPlayer,
		videoSrc = this.getState().videoSrc,
		mimeType = this.getState().mimetype;
	console.log("\n---------------------");
	console.log("sph_vaadin_ui_videojs_Videojs() {");
	videoPlayer = videoMcq.init($(this.getElement()), this.getState());
	videoPlayer.setEventCallback(this.videoEventFired);

	/**
	 * Method gets executed when the Vaadin server attempts to update client values.
	 *
	 * @function window.sph_vaadin_ui_videojs_Videojs~onStateChange
	 */
	this.onStateChange = function () {
		// videoMcq.init($(this.getElement()), that.getState(), callback);
		console.log("\tonStateChange() {");
		//this.executeUpdate(this.getState().actionFlag);
		//var actionFlag = this.getState().actionFlag;
		//actionFlags.push(this.getState().actionFlag);
		//console.log("\actionFlags: " + actionFlags);
		if (videoSrc !== this.getState().videoSrc || mimeType !== this.getState().mimetype) {
			videoSrc = this.getState().videoSrc;
			mimeType = this.getState().mimetype;
			videoPlayer.dispose();
			videoPlayer = videoMcq.init($(this.getElement()), this.getState());
			videoPlayer.setEventCallback(this.videoEventFired);
		}
		console.log("\t}");
	};

	/**
	 * Plays the video stream.
	 *
	 * @function window.sph_vaadin_ui_videojs_Videojs~play
	 * @fires external:videojs#play
	 * @see      {@link videoMcq.VideoPlayer#play videoMcq.VideoPlayer.play()}
	 */
	this.play = function () {
		videoPlayer.play();
	};

	/**
	 * Pauses the video stream playback.
	 *
	 * @function window.sph_vaadin_ui_videojs_Videojs~pause
	 * @fires    external:videojs#pause
	 * @see      {@link videoMcq.VideoPlayer#pause videoMcq.VideoPlayer.pause()}
	 */
	this.pause = function () {
		videoPlayer.pause();
	};

	/**
	 * Seeks to the given time in the video stream and pauses the playback.
	 *
	 * @function window.sph_vaadin_ui_videojs_Videojs~seekTo
	 * @param    {Number} time the time
	 * @see      {@link videoMcq.VideoPlayer#seekTo videoMcq.VideoPlayer.seekTo(time)}
	 */
	this.seekTo = function (time) {
		videoPlayer.seekTo(time);
	};

	/**
	 * Pauses the video playback at given time(s).
	 *
	 * @function window.sph_vaadin_ui_videojs_Videojs~pauseAt
	 * @param    {Array.<number>} times a collection of times in seconds where the video stream is paused.
	 * @see      {@link videoMcq.VideoPlayer#pauseAt videoMcq.VideoPlayer.pauseAt(times)}
	 */
	this.pauseAt = function (times) {
		videoPlayer.pauseAt(times);
	};

	/**
	 * Displays a mask over the Video.js player element.
	 *
	 * @function window.sph_vaadin_ui_videojs_Videojs~mask
	 * @param    {String} [label=null] Text message that will be displayed on top of the mask (optional).
	 * @param    {Number} [delay=0] Delay in milliseconds before player element is masked (optional).
	 * @see      {@link videoMcq.VideoPlayer#mask videoMcq.VideoPlayer.mask(label, delay)}
	 */
	this.mask = function (label, delay) {
		videoPlayer.mask(label, delay);
	};

	/**
	 * Removes the mask from the Video.js player element.
	 *
	 * @function window.sph_vaadin_ui_videojs_Videojs~unmask
	 * @see      {@link videoMcq.VideoPlayer#unmask videoMcq.VideoPlayer.dispose()}
	 */
	this.unmask = function () {
		videoPlayer.unmask();
	};

	/**
	 * Changes the width and height of the Video.js player to the supplied width and height.
	 *
	 * @function window.sph_vaadin_ui_videojs_Videojs~setDimensions
	 * @param    {Number} width The width in pixels
	 * @param    {Number} height The height in pixels
	 * @fires    external:videojs#resize
	 * @see      {@link videoMcq.VideoPlayer#setDimensions videoMcq.VideoPlayer.setDimensions(width, height)}
	 */
	this.setDimensions = function (width, height) {
		videoPlayer.setDimensions(width, height);
	};

	/**
	 * Specifies whether the {@link external:videojs videojs} player is set to full window mode.
	 *
	 * @function window.sph_vaadin_ui_videojs_Videojs~setDimensions
	 * @param    {boolean} fullWindow specifies whether the {@link external:videojs videojs} player is set to full window mode.
	 * @fires    external:videojs#resize
	 * @see      {@link videoMcq.VideoPlayer#setFullWindow videoMcq.VideoPlayer.setFullWindow(fullWindow)}
	 */
	this.setFullWindow = function (fullWindow) {
		videoPlayer.setFullWindow(fullWindow);
	};

	/**
	 * Specifies whether the {@link external:videojs videojs} player has minimal user controls.
	 *
	 * @function window.sph_vaadin_ui_videojs_Videojs~minimalUserControlsEnabled
	 * @param    {boolean} minimalized specifies whether the {@link external:videojs videojs} player is set to full window mode.
	 * @see      {@link videoMcq.VideoPlayer#setMinimalized videoMcq.VideoPlayer.setMinimalized(minimalized)}
	 */
	this.minimalUserControlsEnabled = function (minimalized) {
		videoPlayer.minimalUserControlsEnabled(minimalized);
	};

	/**
	 * Specifies whether the {@link external:videojs videojs} player supports full screen mode.
	 *
	 * @function window.sph_vaadin_ui_videojs_Videojs~supportFullscreenMode
	 * @param    {boolean} enabled specifies whether the {@link external:videojs videojs} player supports full screen mode.
	 * @see      {@link videoMcq.VideoPlayer#supportFullscreenMode videoMcq.VideoPlayer.supportFullscreenMode(enabled)}
	 */
	this.supportFullscreenMode = function (enabled) {
		videoPlayer.supportFullscreenMode(enabled);
	};

	/**
	 * Disposes the inner {@link external:videojs videojs} player
	 *
	 * @function window.sph_vaadin_ui_videojs_Videojs~dispose
	 * @see      {@link videoMcq.VideoPlayer#dispose videoMcq.VideoPlayer.dispose()}
	 */
	this.dispose = function () {
		videoPlayer.dispose();
	};
	console.log("} sph_vaadin_ui_videojs_Videojs");
	console.log("---------------------\n");

	/**
	 * Callback method for sending {@link Videojs Video.js} event to the server.
	 *
	 * @function window.sph_vaadin_ui_videojs_Videojs~videoEventFired
	 * @param    {String} eventName the name of the {@link Videojs} event.
	 * @param    {Number} time the time when the Video.js event was fired.
	 * @param    {Number} [duration] the duration of the played video stream.
	 */
};