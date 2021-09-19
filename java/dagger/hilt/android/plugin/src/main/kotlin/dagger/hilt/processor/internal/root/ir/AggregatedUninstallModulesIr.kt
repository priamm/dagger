package dagger.hilt.processor.internal.root.ir

import com.squareup.javapoet.ClassName

data class AggregatedUninstallModulesIr(
  val fqName: ClassName,
  val test: ClassName,
  val uninstallModules: List<ClassName>
)