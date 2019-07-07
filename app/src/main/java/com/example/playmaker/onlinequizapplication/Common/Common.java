package com.example.playmaker.onlinequizapplication.Common;

import com.example.playmaker.onlinequizapplication.model.Question;
import com.example.playmaker.onlinequizapplication.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by playmaker on 4/6/2018.
 */

public class Common {
    public static String categoryID, categoryName;
    public static User currentUser;
    public static List<Question> questionList = new ArrayList<>();
    public static final String STR_PUSH = "pushNotification";
}
