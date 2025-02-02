package com.example.projectcubes42.testSystemes;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

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
    public void testButtonAddEmployeeAdmin() {
        // Cliquer 5 fois sur l'image
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
        onView(withId(R.id.login)).perform(click());

        // Cliquer sur le bouton "Ajouter un employé"
        onView(withId(R.id.button_add_employee))
                .check(matches(isDisplayed()))
                .perform(click());

        // Saisie des informations de l'employé
        onView(withId(R.id.editTextName))
                .perform(typeText("Anatol"), closeSoftKeyboard());
        onView(withId(R.id.editTextFirstname))
                .perform(typeText("Paul"), closeSoftKeyboard());
        onView(withId(R.id.editTextPhone))
                .perform(typeText("0695784123"), closeSoftKeyboard());
        onView(withId(R.id.editTextTextEmailAddress))
                .perform(typeText("Anatol@gmail.com"), closeSoftKeyboard());

        // Interaction avec le Spinner
        onView(withId(R.id.spinnerService))
                .check(matches(isDisplayed()))
                .perform(click());

        // Vérifier que l'option "Secoe" est affichée et la sélectionner
        onView(withText("Secoe"))
                .check(matches(isDisplayed()))
                .perform(click());

        // Vérifier que "Secoe" est bien sélectionné dans le Spinner
        onView(withId(R.id.spinnerService))
                .check(matches(withSpinnerText("Secoe")));

        onView(withId(R.id.spinnerSite))
                .check(matches(isDisplayed()))
                .perform(click());

        // Vérifier que l'option "Secoe" est affichée et la sélectionner
        onView(withText("Rouen"))
                .check(matches(isDisplayed()))
                .perform(click());

        // Vérifier que "Secoe" est bien sélectionné dans le Spinner
        onView(withId(R.id.spinnerSite))
                .check(matches(withSpinnerText("Rouen")));

        onView(withId(R.id.buttonSendForm))
                .check(matches(isDisplayed()))
                .perform(click());

        // Ajouter des assertions supplémentaires si nécessaire
        // Par exemple, vérifier qu'un message de succès apparaît
        // onView(withText("Employé ajouté avec succès")).check(matches(isDisplayed()));
    }
}
