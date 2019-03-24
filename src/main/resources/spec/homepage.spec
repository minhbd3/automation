@script
    menu_element = ["", "Video", "Thời sự", "Góc nhìn", "Thế giới", "Kinh doanh", "Giải trí", "Thể thao", "Pháp luật", "Giáo dục", "Sức khỏe", "Đời sống",
                    "Du lịch", "Khoa học", "Số hóa", "Xe", "Ý kiến", "Tâm sự", "Cười"];
@objects
    logo_vne             xpath       //*[@class='left logo']/img
    main_menu            xpath       //*[@id='main_menu']
    main_menu_item-*     xpath       //*[@id='main_menu']/a

    myvne_contact        xpath       //*[@class='myvne_contact vcard']
    myvne_form           xpath       //*[@class='myvne_form_log']
    myvne_contact_item-* xpath       //*[@class='myvne_contact vcard']/li
    myvne_form_item-*    xpath       //*[@class='myvne_form_log']/li
    timer                xpath       //*[@class='timer']
    24h_over             xpath       //*[@href='https://vnexpress.net/24h-qua']
    rss                  xpath       //*[@href='https://vnexpress.net/rss']
    fb_like_btn          id          myvne_fb_like
    tw_follow_btn        id          myvne_fb_twitter
    search_box           id          inp_keyword
    search_icon          xpath       //*[@class='icon_seach_web']
    login_btn            xpath       //*[@class='myvne_user']
    register_btn         xpath       //*[@class='myvne_notification']

    #main_box
    main_thumb           xpath       //*[@class='thumb_big']
    main_title           xpath       //h1[@class='title_news']/a
    main_description     xpath       //h1[@class='title_news']//following::p[1]

    #sub_feature
    sub_feature_box      id          list_sub_featured
    sub_feature_item-*   xpath       //*[@id='list_sub_featured']/li

    #top_contact
    top_contact_phone    xpath      //*[@class='top-contact']
    top_contact_ads      xpath      //*[@title='quảng cáo']
@on desktop
    @groups
        myvne_taskbar   timer, 24h_over, rss, fb_like_btn, tw_follow_btn, search_box, search_icon, login_btn, register_btn, myvne_contact_item-*, myvne_form_item-*, myvne_contact, myvne_form
        logo_and_footer logo_vne, main_menu, main_menu_item-*
        main_box        main_thumb, main_title, main_description
        sub_feature     sub_feature_box,  sub_feature_item-*
        top_contact     top_contact_phone, top_contact_ads
    = Visible =
        &myvne_taskbar:
            visible
        &logo_and_footer:
            visible
        &main_box:
            visible
        &sub_feature:
            visible
        &top_contact:
            visible
    = Count =
        main_menu_item-*:
            count any main_menu_item-* is 19
        myvne_contact_item-*:
            count any myvne_contact_item-* is 4
        myvne_form_item-*:
            count any myvne_form_item-* is 3
        sub_feature_item-*:
            count any sub_feature_item-* is 10
    = Text =
        @forEach [main_menu_item-*] as item, index as i
            ${item}:
                text is "${menu_element[i-1]}"
    = Ranges =
    #myvne_taskbar
        timer:
           width ~187px
           height ~14px
        24h_over:
           width ~45px
           height ~29px
        rss:
           width ~25px
           height ~29px
        fb_like_btn:
           width ~50px
           height ~21px
        tw_follow_btn:
           width ~60px
           height ~20px
        search_box:
           width ~150px
           height ~23px
        search_icon:
           width ~23px
           height ~23px
        login_btn:
           width ~75px
           height ~29px
        register_btn:
           width ~55px
           height ~29px
        myvne_contact:
           width ~423px
           height ~29px
        myvne_form:
           width ~310px
           height ~29px
    #logo_and_footer
        logo_vne:
            width ~225px
            height ~101px
        main_menu:
            width ~1000px
            height ~20px
    #main_box
        main_thumb:
            width ~500px
            height ~300px
        main_title:
            width ~435px
            height ~22px
        main_description:
            width ~500px
            height ~32px
    #sub_feature
        sub_feature_box:
            width ~169px
            height ~417px
        sub_feature_item-*:
            width ~160px
            height 38 to 56px
    #top_contact
        top_contact_phone:
            width ~239px
            height ~12px
        top_contact_ads:
            width ~133px
            height ~25px