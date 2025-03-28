// swift-tools-version: 5.9
import PackageDescription

let package = Package(
    name: "CapacitorPluginAmap",
    platforms: [.iOS(.v13)],
    products: [
        .library(
            name: "CapacitorPluginAmap",
            targets: ["AMapPlugin"])
    ],
    dependencies: [
        .package(url: "https://github.com/ionic-team/capacitor-swift-pm.git", branch: "main")
    ],
    targets: [
        .target(
            name: "AMapPlugin",
            dependencies: [
                .product(name: "Capacitor", package: "capacitor-swift-pm"),
                .product(name: "Cordova", package: "capacitor-swift-pm")
            ],
            path: "ios/Sources/AMapPlugin"),
        .testTarget(
            name: "AMapPluginTests",
            dependencies: ["AMapPlugin"],
            path: "ios/Tests/AMapPluginTests")
    ]
)