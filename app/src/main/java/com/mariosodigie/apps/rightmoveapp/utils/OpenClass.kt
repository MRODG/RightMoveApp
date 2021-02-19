package com.mariosodigie.apps.rightmoveapp.utils

/**
 * This annotation allows us to open some classes for mocking purposes while they are final in
 * production builds.
 */
@Target(AnnotationTarget.ANNOTATION_CLASS)
annotation class OpenClass

/**
 * Annotate a class with [OpenForTesting] if you want it to be extendable/mockable in test builds.
 */
@OpenClass
@Target(AnnotationTarget.CLASS)
annotation class OpenForTesting