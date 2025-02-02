package com.example.projectcubes42.testSystemes;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
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
        for (int i = 0; i < 5; i++) {
            onView(withId(R.id.imageViewVisitor)).perform(click());
        }

        // Saisie du user Admin
        onView(withId(R.id.username))
                .perform(typeText("Paul"), closeSoftKeyboard());

        // Saisie du password Admin et fermeture du clavier
        onView(withId(R.id.password))
                .perform(typeText("1234"), closeSoftKeyboard());

        // Cliquer sur le bouton de connexion
        onView(withId(R.id.login))
                .perform(click());



}}
