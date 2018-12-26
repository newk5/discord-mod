"use strict";
module.exports = {
    init: function (token, events, options) {

        DiscordWrapper.init(token, events,options);

        let obj = {
            botToken: token,
            send: function (channel, msg) {
                DiscordWrapper.send(token, channel, msg);
            },
            message: function (userName, msg) {
                DiscordWrapper.message(token, userName, msg);
            }
        }
        return obj;
    }
}