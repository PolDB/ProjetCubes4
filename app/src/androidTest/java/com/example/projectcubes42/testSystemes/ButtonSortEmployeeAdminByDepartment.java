package com.example.projectcubes42.testSystemes;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.ActivityTestRule;

import com.example.projectcubes42.MainActivity;
import com.example.projectcubes42.R;

import org.junit.Rule;
import org.junit.Test;

public class ButtonSortEmployeeAdminByDepartment {

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

        onView(ViewMatchers.withId(R.id.button_sort_department))
                .perform(click());

        // Vérifier que l'AlertDialog est affichée avec un titre spécifique
        onView(withText("Filtrer par services"))
                .inRoot(isDialog())
                .check(matches(withText("Filtrer par services")));

        // Optionnel : Vérifier la présence d'un bouton spécifique dans l'AlertDialog et cliquer dessus
        onView(withText("STage"))
                .inRoot(isDialog())
                .check(matches(withText("STage")))
                .perform(click());


    }}
