package com.example.projectcubes42;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.containsString;

import com.example.projectcubes42.ui.employee.AddEmployee;

@RunWith(AndroidJUnit4.class)
public class SpinnerTest {

    @Rule
    public ActivityScenarioRule<AddEmployee> activityRule =
            new ActivityScenarioRule<>(AddEmployee.class);

    @Before
    public void setUp() {
        // Initialisation si nécessaire
    }

    @Test
    public void testSpinnerSelection() {
        // 1) On clique sur le spinner
        onView(withId(R.id.spinnerSite)).perform(click());

        // 2) On sélectionne le 2ème item (par exemple index=1 s’il y a un placeholder)
        onData(anything()) // Correspond à "peu importe l'objet"
                .atPosition(1) // on prend le 2ème item
                .inRoot(isPlatformPopup()) // on précise que c'est dans la popup du Spinner
                .perform(click());

        // 3) On vérifie que le spinner affiche le texte correspondant à cet item
        onView(withId(R.id.spinnerSite))
                .check(matches(withSpinnerText(containsString("Direction"))));
    }
}
