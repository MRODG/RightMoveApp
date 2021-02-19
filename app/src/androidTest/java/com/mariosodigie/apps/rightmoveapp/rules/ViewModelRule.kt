package com.mariosodigie.apps.rightmoveapp.rules

import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.executor.TaskExecutor
import androidx.lifecycle.ViewModel
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.rules.MethodRule
import org.junit.runners.model.FrameworkMethod
import org.junit.runners.model.Statement
import org.koin.androidx.viewmodel.dsl.setIsViewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.definition.BeanDefinition
import org.koin.core.definition.Kind
import org.koin.core.definition.Options
import org.koin.dsl.module
import org.mockito.Mock
import org.mockito.MockitoAnnotations

/**
 * This rule has two purposes:
 * 1) makes [LiveData] post updates synchronously
 * 2) overrides [ViewModel]s in Koin with the [Mock]s defined in test
 */

class ViewModelRule : MethodRule {

    override fun apply(base: Statement, method: FrameworkMethod?, target: Any) = object : Statement() {
        override fun evaluate() {
            setTaskExecutorDelegate()
            loadMocks(target)

            try {
                base.evaluate()
            } finally {
                ArchTaskExecutor.getInstance().setDelegate(null)
            }
        }
    }

    private fun setTaskExecutorDelegate() {
        ArchTaskExecutor.getInstance().setDelegate(object : TaskExecutor() {
            override fun executeOnDiskIO(runnable: Runnable) = runnable.run()
            override fun isMainThread() = true
            override fun postToMainThread(runnable: Runnable)
                    = InstrumentationRegistry.getInstrumentation().runOnMainSync(runnable)
        })
    }

    private fun loadMocks(target: Any) {
        MockitoAnnotations.initMocks(target)

        val mockedViewModels = target.javaClass.fields
            .filter {
                it.isAnnotationPresent(Mock::class.java) && ViewModel::class.java.isAssignableFrom(it.type)
            }
            .map { field ->
                BeanDefinition<ViewModel>(primaryType = field.type.kotlin).apply {
                    kind = Kind.Factory
                    setIsViewModel()
                    definition = { field.get(target) as ViewModel }
                }
            }

        loadKoinModules(module {
            mockedViewModels.forEach { definition ->
                declareDefinition(definition, options = Options(override = true))
            }
        })
    }
}