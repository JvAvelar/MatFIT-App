package engsoft.matfit.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

class RegraTestCustomizada : TestWatcher(){

    override fun starting(description: Description?) {
        super.starting(description)
        Dispatchers.setMain(StandardTestDispatcher())
    }

    override fun finished(description: Description?) {
        super.finished(description)
        Dispatchers.resetMain()
    }

}