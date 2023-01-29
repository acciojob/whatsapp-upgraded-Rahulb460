package com.driver;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Repository
public class WhatsappRepository {

    HashMap<String, User> userHashMap = new HashMap<>();
    HashMap<Group, List<User>> groupUserHashMap = new HashMap<>();
    HashMap<User,List<Message>> userMessageMap = new HashMap<>();
    HashMap<Group,List<Message>> groupMessageMap = new HashMap<>();
    List<Message> messageList = new ArrayList<>();

    int groupId = 1;
    int messageID = 0;
    public String createUser(String name,String mobile) throws Exception {
        if(userHashMap.containsKey(mobile)) {
            throw new Exception("User already exists");
        }
        else{
            User user = new User(name, mobile);
            userHashMap.put(mobile, user);
            return "SUCCESS";
        }
    }

    public Group createGroup(List<User> users){
        Group group = new Group();
        if (users.size() == 2) {
            User user = users.get(1);
            group.setName(user.getName());
            group.setNumberOfParticipants(2);
        }
        else {
            group.setName("Group " + groupId);
            group.setNumberOfParticipants(users.size());
            groupId++;
        }
        groupUserHashMap.put(group, users);
        return  group;
    }

    public int createMessage(String content){
        Message message = new Message();
        message.setContent(content);
        message.setId(++messageID);
        message.setTimestamp(new Date());
        messageList.add(message);
        return messageID;
    }

    public int sendMessage(Message message, User sender, Group group) throws Exception {
        if(!groupUserHashMap.containsKey(group)){
             throw new Exception("Group does not exist");
        }
        boolean flag = false;
        List<User> userList = groupUserHashMap.get(group);
       for(User user : userList) {
           if (user == sender){
               flag = true;
               break;
           }
       }
       if (!flag) throw new Exception("You are not allowed to send message");

        if(groupMessageMap.containsKey(group)){
            groupMessageMap.get(group).add(message);
        }
        else {
            List<Message> messages = new ArrayList<>();
            messages.add(message);
            groupMessageMap.put(group,messages);
        }

        if(userMessageMap.containsKey(sender)){
            userMessageMap.get(sender).add(message);
        }
        else {
            List<Message> messages = new ArrayList<>();
            messages.add(message);
            userMessageMap.put(sender,messages);
        }

        return groupMessageMap.get(group).size();
    }

//    public String changeAdmin(User approver, User user, Group group) throws Exception{
//        if(!groupUserHashMap.containsKey(group)) throw new Exception("Group does not exist");
//
//        if(groupUserHashMap.get(group).get(0) != approver) throw new Exception("Approver does not have rights");
//
//    }

}
