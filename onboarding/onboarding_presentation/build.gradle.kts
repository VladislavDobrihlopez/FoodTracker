apply {
    from("$rootDir/compose-module.gradle")
}

dependencies {
    "implementation"(project(Modules.commonUi))
    "implementation"(project(Modules.common))
    "implementation"(project(Modules.onboardingDomain))
}
