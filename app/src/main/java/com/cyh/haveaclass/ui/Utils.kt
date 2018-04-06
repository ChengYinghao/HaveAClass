package com.cyh.haveaclass.ui

import android.os.Bundle

internal operator fun Bundle?.plus(bundle: Bundle): Bundle = (this ?: Bundle()).apply { putAll(bundle) }
