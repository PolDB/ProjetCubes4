package com.example.projectcubes42.testSystemes;

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

public class ButtonAddEmployeeAdmin {
    @Rule
    public ActivityTestRule<MainActivity> activityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testSearchButtonDisplaysAlertDialog() {
        // Cliquer 5 fois sur l'image
        for (int i = 0; i < 5; i++) {
            onView(withId(R.id.imageViewVisitor)).perform(click());
        }

        // Saisie du user Admin
        onView(withId(R.id.username)).perform(typeText("Paul"));

        // Saisie du password Admin et fermeture du clavier
        onView(withId(R.id.password)).perform(typeText("1234"), closeSoftKeyboard());

        // Cliquer sur le bouton de connexion
        onView(withId(R.id.login)).perform(click());

        // Cliquer sur le bouton "Ajouter un employé"
        onView(withId(R.id.button_add_employee)).perform(click());
    }
}
