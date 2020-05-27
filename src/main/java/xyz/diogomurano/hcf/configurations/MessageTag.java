package xyz.diogomurano.hcf.configurations;

public enum MessageTag {

    VALUE_MUST_BE_A_NUMBER("value-must-be-a-number"),

    CHAT_CONTROL_ACTIVATE_ALREADY_STATUS("chat-control.activate.already-status"),
    CHAT_CONTROL_ACTIVATE_CHANGED_STATUS("chat-control.activate.changed-status"),
    CHAT_CONTROL_DELAY_MODE_ALREADY_STATUS("chat-control.delay-mode.already-status"),
    CHAT_CONTROL_DELAY_MODE_CHANGED_STATUS("chat-control.delay-mode.changed-status"),
    CHAT_CONTROL_DELAY_VALUE_CHANGED("chat-control.delay.value-changed"),

    CHAT_DISABLED,
    CHAT_WAIT_COOLDOWN;

    private String name;

    MessageTag(String name) {
        this.name = name;
    }

    MessageTag() {
    }

    public String getName() {
        return name == null ? name().replace("_", ".").replace("-", ".").toLowerCase() : name;
    }

}
