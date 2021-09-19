package dagger.hilt.processor.internal.root.ir

import com.squareup.javapoet.ClassName

data class AggregatedRootIr(
  val fqName: ClassName,
  val root: ClassName,
  val originatingRoot: ClassName,
  val rootAnnotation: ClassName,
  // External property from the annotation that indicates if root can use a shared component.
  val allowsSharingComponent: Boolean = true
) {
  // Equivalent to RootType.isTestRoot()
  val isTestRoot = TEST_ROOT_ANNOTATIONS.contains(rootAnnotation.toString())

  companion object {
    private val TEST_ROOT_ANNOTATIONS = listOf(
      "dagger.hilt.android.testing.HiltAndroidTest",
      "dagger.hilt.android.internal.testing.InternalTestRoot",
    )
  }
}
