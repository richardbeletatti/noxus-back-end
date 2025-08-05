package com.alemcrm.dto;

import java.util.List;

public class WhatsappWebhookDTO {
    public String object;
    public List<Entry> entry;

    public static class Entry {
        public String id;
        public List<Change> changes;
    }

    public static class Change {
        public Value value;
        public String field;
    }

    public static class Value {
        public String messaging_product;
        public Metadata metadata;
        public List<Contact> contacts;
        public List<Message> messages;
    }

    public static class Metadata {
        public String display_phone_number;
        public String phone_number_id;
    }

    public static class Contact {
        public Profile profile;
        public String wa_id;
    }

    public static class Profile {
        public String name;
    }

    public static class Message {
        public String from;
        public String id;
        public String timestamp;
        public Text text;
        public String type;
    }

    public static class Text {
        public String body;
    }
}

