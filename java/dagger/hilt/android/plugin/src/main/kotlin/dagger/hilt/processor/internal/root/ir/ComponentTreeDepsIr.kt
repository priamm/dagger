package dagger.hilt.processor.internal.root.ir

import com.squareup.javapoet.ClassName

data class ComponentTreeDepsIr(
  val name: ClassName,
  val rootDeps: Set<ClassName>,
  val defineComponentDeps: Set<ClassName>,
  val aliasOfDeps: Set<ClassName>,
  val aggregatedDeps: Set<ClassName>,
  val uninstallModulesDeps: Set<ClassName>,
  val earlyEntryPointDeps: Set<ClassName>,
)