package br.com.marvel


import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.hamcrest.Matcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class AppFlowTest {

    @Rule
    @JvmField
    var activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun testMainActivityInteraction() {

        // Aguarda 10 segundos para garantir que todos os dados e elementos estejam carregados
        Thread.sleep(10000)

        // Localiza o RecyclerView pelo ID
        val recyclerView = onView(withId(R.id.comic_recyclerview))

        // Verifica se o RecyclerView está visível na tela
        recyclerView.check(ViewAssertions.matches(isDisplayed()))

        // Rola o RecyclerView até o item na posição 3 (4º item, já que a contagem começa em 0)
        recyclerView.perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(3))

        // Clica no ImageButton dentro do item na posição 3 para favoritar
        recyclerView.perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                3, // Posição do item
                clickOnChildView(R.id.imagebutton_favorite) // ID do ImageButton
            )
        )

        // Aguarda 5 segundos para observar o efeito do clique
        Thread.sleep(5000)

        // Localiza e clica no FloatingActionButton para visualizar favoritos
        val favoritesButton = onView(withId(R.id.fab_favorites))
        favoritesButton.perform(click())

        // Aguarda 5 segundos para garantir que a ação foi processada
        Thread.sleep(5000)

        // Localiza e clica no botão de voltar
        val backButton = onView(withId(R.id.button_back))
        backButton.perform(click())

        // Aguarda 5 segundos para garantir que o retorno à tela anterior foi completado
        Thread.sleep(5000)

        // Clica no botão de personagens dentro do item na posição 3
        recyclerView.perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                3, // Posição do item
                clickOnChildView(R.id.button_character) // ID do botão dentro do item
            )
        )

        // Aguarda 5 segundos para observar o efeito do clique
        Thread.sleep(5000)

        // Clica no botão de voltar novamente para retornar à tela anterior
        backButton.perform(click())

        // Aguarda 10 segundos antes de finalizar o teste
        Thread.sleep(10000)
    }

    private fun clickOnChildView(viewId: Int) = object : ViewAction {
        override fun getConstraints(): Matcher<View> {
            return isAssignableFrom(View::class.java) // Garante que a View é válida
        }

        override fun getDescription(): String {
            return "Click on a child view with specified id." // Descrição da ação
        }

        override fun perform(uiController: UiController, view: View) {
            val childView = view.findViewById<View>(viewId) // Localiza a View filha pelo ID
            childView.performClick() // Executa o clique na View
        }
    }
}

