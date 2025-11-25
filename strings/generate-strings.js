const fs = require("fs");
const path = require("path");

const VERSION = "1.1";
const DEFAULT_LANG = "en";
// const IOS_FILE = "./iosDir/Localizable.xcstrings"
// const ANDROID_DIR = "./androidDir"
const IOS_FILE = "../iosApp/Localizable.xcstrings";
const ANDROID_DIR = "../composeApp/src/androidMain/res";
const STRINGS_FILE = "./strings.txt";
const CLEAR_ON_RUN = true;

if (CLEAR_ON_RUN) {
  removeExistingStrings();
}

console.log("Reading strings file...");
const stringsData = fs.readFileSync(STRINGS_FILE, { encoding: "utf8" });

const stringsJson = parseStringsFile(stringsData);
const iOS_Strings = convertToIosStrings(stringsJson);
const iosJsonString = JSON.stringify(iOS_Strings, null, 2);
const androidFiles = generateAndroidStringsXML(stringsJson);

try {
  fs.writeFileSync(IOS_FILE, iosJsonString);
  console.log(`✅ iOS strings file written to ${IOS_FILE}`);
} catch (err) {
  console.error("Error writing iOS file", err);
}

for (const [output, content] of Object.entries(androidFiles)) {
  try {
    if (!fs.existsSync(output)) {
      fs.mkdirSync(output, { recursive: true });
    }
    fs.writeFileSync(`${output}/strings.xml`, content);
    console.log(`✅ Android strings written to ${output}`);
  } catch (err) {
    console.error("Filed to write Android file: ", output, err);
  }
}

function parseStringsFile(text) {
  const lines = text.split(/\r?\n/);
  const json = {};
  let currentSection = null;
  let currentSubSection = null;

  lines.forEach((line) => {
    line = line.trim();
    if (!line) return; // skip empty lines

    // Match [[Section]]
    const sectionMatch = line.match(/^\[\[(.+?)\]\]$/);
    if (sectionMatch) {
      currentSection = sectionMatch[1];
      json[currentSection] = {};
      currentSubSection = null;
      return;
    }

    // Match [subsection]
    const subSectionMatch = line.match(/^\[(.+?)\]$/);
    if (subSectionMatch && currentSection) {
      currentSubSection = subSectionMatch[1];
      json[currentSection][currentSubSection] = {};
      return;
    }

    // Match key = value lines under subsection
    const keyValueMatch = line.match(/^(.+?)\s*=\s*(.+)$/);
    if (keyValueMatch && currentSection && currentSubSection) {
      const key = keyValueMatch[1].trim();
      const value = keyValueMatch[2].trim();
      json[currentSection][currentSubSection][key] = value;
      return;
    }
  });

  return json;
}

function convertToIosStrings(input) {
  console.log("Generating iOS strings");
  const output = {
    sourceLanguage: "en",
    strings: {},
    version: "1.1",
  };

  for (const sectionKey in input) {
    if (!input.hasOwnProperty(sectionKey)) continue;

    const section = input[sectionKey];
    for (const subKey in section) {
      if (!section.hasOwnProperty(subKey)) continue;

      const subSection = section[subKey];
      const obj = {
        comment: subSection.comment || undefined,
        extractionState: "manual",
        localizations: {},
      };

      for (const locale of Object.keys(subSection)) {
        if (locale === "comment") continue;
        obj.localizations[locale] = {
          stringUnit: {
            state: "translated",
            value: subSection[locale],
          },
        };
      }
      output.strings[subKey] = obj;
    }
  }
  console.log("iOS Strings generated");

  return output;
}

function generateAndroidStringsXML(json) {
  console.log("Generating Android strings files...");
  const supportedLangs = new Set();

  // Gather all supported languages (except comments)
  for (const category in json) {
    for (const key in json[category]) {
      Object.keys(json[category][key]).forEach((lang) => {
        if (lang !== "comment") supportedLangs.add(lang);
      });
    }
  }

  const langFiles = {};

  supportedLangs.forEach((lang) => {
    const dir =
      lang === DEFAULT_LANG
        ? `${ANDROID_DIR}/values`
        : `${ANDROID_DIR}/values-${lang}`;
    let xmlContent = `<resources>\n`;

    for (const category in json) {
      // Filter keys in this category that have the current lang
      const keysForLang = Object.keys(json[category]).filter(
        (key) => lang in json[category][key],
      );

      if (keysForLang.length === 0) continue; // Skip category with no keys for this lang

      // Add category comment
      xmlContent += `    <!-- ${category} -->\n`;

      keysForLang.forEach((key) => {
        const entry = json[category][key];

        // Escape XML special chars in value
        const value = entry[lang]
          .replace(/&/g, "&amp;")
          .replace(/</g, "&lt;")
          .replace(/>/g, "&gt;")
          .replace(/"/g, "&quot;")
          .replace(/'/g, "&apos;");

        xmlContent += `    <string name="${key}">${value}</string>`;

        // Add comment only for default language and if comment exists
        if (lang === DEFAULT_LANG && entry.comment) {
          xmlContent += `  <!-- ${entry.comment} -->\n`;
        } else {
          xmlContent += "\n";
        }
      });

      xmlContent += `\n`; // gap between categories
    }

    xmlContent += `</resources>\n`;
    langFiles[dir] = xmlContent;
    console.log(`Android file generated for ${lang}`);
  });
  console.log("Android string files generated");

  return langFiles;
}

function removeExistingStrings() {
  console.log("Removing existing strings");

  const androidEntries = fs.readdirSync(ANDROID_DIR, { withFileTypes: true });

  androidEntries.forEach((entry) => {
    if (entry.isDirectory()) {
      const folderName = entry.name;

      if (folderName === "values" || /^values-\w+$/.test(folderName)) {
        const fullPath = path.join(ANDROID_DIR, folderName);
        try {
          fs.rmSync(fullPath, { recursive: true, force: true });
          console.log(`⌦ Deleted android values: ${fullPath}`);
        } catch (err) {
          console.error(`Failed to delete folder ${fullPath};`, err);
        }
      }
    }
  });

  try {
    fs.rmSync(IOS_FILE, { force: true });
    console.log(`⌦ Deleted iOS file: ${IOS_FILE}`);
  } catch (err) {
    console.error(`Failed to delete iOS file ${IOS_FILE}`, err);
  }
}
