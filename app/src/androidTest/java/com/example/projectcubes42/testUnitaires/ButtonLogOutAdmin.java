package com.example.projectcubes42.testUnitaires;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.ActivityTestRule;

import com.example.projectcubes42.MainActivity;
import com.example.projectcubes42.R;

import org.junit.Rule;
import org.junit.Test;

public class ButtonLogOutAdmin {
    @Rule
    public ActivityTestRule<MainActivity> activityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testSearchButtonDisplaysAlertDialog() {
        // Cliquer sur le bouton de recherche
        onView(withId(R.id.imageViewVisitor))
                .perform(click());
        //Saisie du user Admin
        onView(withId(R.id.username))
                .perform(typeText("Paul"));
        //Saisie du password Admin
        onView(withId(R.id.password)).perform(typeText("1234") ,closeSoftKeyboard());
        //Cliquer sur le bouton de connexino
        onView(ViewMatchers.withId(R.id.login))
                .perform(click());
        onView(ViewMatchers.withId(R.id.action_settings))
                .perform(click());
}}
