package com.example.projectcubes42.testSystemes;

import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;

import com.example.projectcubes42.MainActivity;
import com.example.projectcubes42.R;

@RunWith(AndroidJUnit4.class)
public class ButtonSearchEmployeeVisitor {

    @Rule
    public ActivityTestRule<MainActivity> activityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testSearchButtonDisplaysAlertDialog() {
        // Cliquer sur le bouton de recherche
        onView(ViewMatchers.withId(R.id.button_search_employee_visitor))
                .perform(click());

        // Vérifier que l'AlertDialog est affichée avec un titre spécifique
        onView(withText("Rechercher un employé"))
                .inRoot(isDialog())
                .check(matches(withText("Rechercher un employé")));

        // Optionnel : Vérifier la présence d'un bouton spécifique dans l'AlertDialog et cliquer dessus
        onView(withText("RECHERCHER"))
                .inRoot(isDialog())
                .check(matches(withText("RECHERCHER")))
                .perform(click());
    }
}
