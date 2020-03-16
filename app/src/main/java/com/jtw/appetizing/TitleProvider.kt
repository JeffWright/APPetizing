package com.jtw.appetizing

/** Something (typically a Fragment) that provides a title to be displayed in the app bar */
interface TitleProvider {

    /** The title to be displayed.  If null, the default (the app name) will be used instead */
    val title: CharSequence?
}