package dagger.hilt.processor.internal.root.ir

import com.squareup.javapoet.ClassName

data class AggregatedDepsIr(
  val fqName: ClassName,
  val components: List<ClassName>,
  val test: ClassName?,
  val replaces: List<ClassName>,
  val module: ClassName?,
  val entryPoint: ClassName?,
  val componentEntryPoint: ClassName?,
)