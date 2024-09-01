# Maven Dependency Download Plugin

This IntelliJ IDEA plugin helps manage and download dependencies specified in Maven `pom.xml` files more effectively.

## Features

- **Fix for Dependency Download Issues**: Resolves issues where dependencies defined in Maven `pom.xml` files fail to download.
- **Basic Download Functionality**: In this version (V1.0), the plugin supports basic dependency downloads. After initiating a download, please wait a few moments before restarting your project.
- **Download Filtered Dependencies**: When filtering for dependencies that have not yet been downloaded, the "Download All" button allows you to download all the filtered dependencies at once.

## Installation

1. Download the plugin from the JetBrains Plugin Repository or from the GitHub release page.
2. Install the plugin by navigating to `File | Settings | Plugins | Install Plugin from Disk` and selecting the downloaded `.zip` file.
3. Restart IntelliJ IDEA to activate the plugin.

## Usage

1. Open any Maven-based project in IntelliJ IDEA.
2. Navigate to your `pom.xml` file.
3. Use the plugin's features to manage your project's dependencies, ensuring all required libraries are downloaded and available.

## Compatibility

This plugin is compatible with IntelliJ IDEA and other JetBrains IDEs that support Maven. The following dependencies are required:

- `com.intellij.modules.platform`
- `org.jetbrains.idea.maven`

## Development

### Build

To build the plugin from source:

1. Clone the repository.
2. Open the project in IntelliJ IDEA.
3. Run the Gradle task `buildPlugin` to generate the `.zip` file.

### Contributing

Contributions are welcome! Please fork the repository and submit a pull request for any features, bug fixes, or improvements.

## Contact

For any questions, issues, or feedback, you can contact the developer at [2384982531@qq.com](mailto:2384982531@qq.com).

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

