package dagger.hilt.processor.internal.root.ir

object AggregatedRootIrValidator {
    @JvmStatic
    @Throws(InvalidRootsException::class)
    fun rootsToProcess(
        isCrossCompilationRootValidationDisabled: Boolean,
        processedRoots: Set<ProcessedRootSentinelIr>,
        aggregatedRoots: Set<AggregatedRootIr>
    ): Set<AggregatedRootIr> {
      val processedRootNames = processedRoots.flatMap { it.roots }.toSet()
      val rootsToProcess = aggregatedRoots
        .filterNot { processedRootNames.contains(it.root) }
        .sortedBy { it.root.toString() }
      val testRootsToProcess = rootsToProcess.filter { it.isTestRoot }
      val appRootsToProcess = rootsToProcess - testRootsToProcess
      fun Collection<AggregatedRootIr>.rootsToString() = map { it.root }.joinToString()
      if (appRootsToProcess.size > 1) {
        throw InvalidRootsException(
          "Cannot process multiple app roots in the same compilation unit: " +
            appRootsToProcess.rootsToString()
        )
      }
      if (testRootsToProcess.isNotEmpty() && appRootsToProcess.isNotEmpty()) {
        throw InvalidRootsException("""
        Cannot process test roots and app roots in the same compilation unit:
          App root in this compilation unit: ${appRootsToProcess.rootsToString()}
          Test roots in this compilation unit: ${testRootsToProcess.rootsToString()}
        """.trimIndent()
        )
      }
      // Perform validation across roots previous compilation units.
      if (!isCrossCompilationRootValidationDisabled) {
        val alreadyProcessedTestRoots = aggregatedRoots.filter {
          it.isTestRoot && processedRootNames.contains(it.root)
        }
        // TODO(b/185742783): Add an explanation or link to docs to explain why we're forbidding this.
        if (alreadyProcessedTestRoots.isNotEmpty() && rootsToProcess.isNotEmpty()) {
          throw InvalidRootsException("""
          Cannot process new roots when there are test roots from a previous compilation unit:
            Test roots from previous compilation unit: ${alreadyProcessedTestRoots.rootsToString()}
            All roots from this compilation unit: ${rootsToProcess.rootsToString()}
          """.trimIndent()
          )
        }
        val alreadyProcessedAppRoots = aggregatedRoots.filter {
          !it.isTestRoot && processedRootNames.contains(it.root)
        }
        if (alreadyProcessedAppRoots.isNotEmpty() && appRootsToProcess.isNotEmpty()) {
          throw InvalidRootsException("""
          Cannot process new app roots when there are app roots from a previous compilation unit:
            App roots in previous compilation unit: ${alreadyProcessedAppRoots.rootsToString()}
            App roots in this compilation unit: ${appRootsToProcess.rootsToString()}
          """.trimIndent()
          )
        }
      }
      return rootsToProcess.toSet()
    }
}

class InvalidRootsException(msg: String) : Exception(msg)