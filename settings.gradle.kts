pluginManagement {
    repositories {
        // Gradle 플러그인은 여기서만 해석됨
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal() // Hilt 등 일부 플러그인이 이 포털을 통해 배포됨
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        // 라이브러리(의존성) 해석용
        google()
        mavenCentral()
    }
}

rootProject.name = "composenetflix"

include(":app")

// 멀티모듈 확장 시:
// include(":core:designsystem", ":core:network", ":feature:home")
