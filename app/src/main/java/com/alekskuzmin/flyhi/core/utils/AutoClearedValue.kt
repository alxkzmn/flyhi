/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alekskuzmin.flyhi.core.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

/**
 * A value holder that automatically clears the reference if the Fragment's view is destroyed.
 *
 * @param callback optional callback to perform additional actions on value clear
 */
class AutoClearedValue<VALUE>(
    fragment: Fragment,
    private var value: VALUE?,
    callback: (() -> Unit)?
) {

    constructor(fragment: Fragment, value: VALUE?) : this(fragment, value, null)

    init {
        val fragmentManager = fragment.fragmentManager
        fragmentManager!!.registerFragmentLifecycleCallbacks(
            object : FragmentManager.FragmentLifecycleCallbacks() {
                override fun onFragmentViewDestroyed(fm: FragmentManager, f: Fragment) {
                    if (f === fragment) {
                        callback?.invoke()
                        this@AutoClearedValue.value = null
                        fragmentManager.unregisterFragmentLifecycleCallbacks(this)
                    }
                }
            }, false
        )
    }

    fun get(): VALUE? {
        return value
    }

}
