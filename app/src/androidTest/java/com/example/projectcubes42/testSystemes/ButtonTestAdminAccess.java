package com.example.projectcubes42.testSystemes;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;

import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.ActivityTestRule;

import com.example.projectcubes42.MainActivity;
import com.example.projectcubes42.R;

import org.junit.Rule;
import org.junit.Test;

public class ButtonTestAdminAccess {


    @Rule
    public ActivityTestRule<MainActivity> activityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testSearchButtonDisplaysAlertDialog() {
        // Cliquer sur le bouton de recherche
        onView(ViewMatchers.withId(R.id.imageViewVisitor))
                .perform(click());
}}
