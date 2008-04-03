package com.calclab.examplechat.client.chatuiplugin.params;

import com.calclab.emite.client.im.chat.Chat;
import com.calclab.examplechat.client.chatuiplugin.users.GroupChatUser.GroupChatUserType;

public class CreateGroupChatActionParam {
    public final Chat groupChat;
    public final String userAlias;
    public final GroupChatUserType groupChatUserType;

    public CreateGroupChatActionParam(final Chat groupChat, final String userAlias,
            final GroupChatUserType groupChatUserType) {
        this.groupChat = groupChat;
        this.userAlias = userAlias;
        this.groupChatUserType = groupChatUserType;
    }

    public Chat getGroupChat() {
        return groupChat;
    }

    public String getUserAlias() {
        return userAlias;
    }

    public GroupChatUserType getGroupChatUserType() {
        return groupChatUserType;
    }

}
