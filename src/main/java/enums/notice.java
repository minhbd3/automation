package enums;

public enum notice {
    INVALID_DOMAIN("{invalid domain} : "),
    ERROR_STATUS_CODE("{notice status code} : "),
    CANT_REQUEST("{can't request} : "),
    ELEMENT_NOT_DISPLAYED(" is not displayed after "),
    ELEMENT_CANT_CLICK("can't click: "),
    ELEMENT_CANT_SCROLL_TO("can't scroll to: "),
    CANT_SCAN_CHARACTER_FROM_IMAGE("can't scan character from image"),
    CANT_DRAG_AND_DROP_ELEMENT("can't drag and drop element"),
    CANT_SEND_VALUE("can't send value: "),
    CANT_SWITCH_FRAME("can't switch frame "),
    CANT_READ_TEXT_FILE("can't read text file from: ");

    private String value;

    notice(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
