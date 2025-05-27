import os
import sys
import xml.etree.ElementTree as ET


def extract_id_type(xml_file):
    tree = ET.parse(xml_file)
    root = tree.getroot()

    # Extract id from /EventData/name/id
    id_element = root.find(".//name/id")

    if id_element is None:
        print("Error: /EventData/name/id element not found")
        return

    id_value = int(id_element.text)

    # Extract type from /EventData/substances/type
    type_element = root.find(".//substances/type")

    if type_element is None:
        print("Error: /EventData/substances/type element not found")
        return

    type_value = int(type_element.text)

    return (id_value, type_value)


if __name__ == "__main__":
    lines = ["INSERT INTO chusan_game_event (id, type, end_date, start_date, enable)\nVALUES\n"]

    if len(sys.argv) < 2:
        print("Usage: python chusan-events.py <dir_name>...")
        exit(1)

    for dir_name in sys.argv[1:]:
        event_dir = os.path.join(dir_name, "event")

        if not os.path.exists(event_dir):
            continue

        for sub_dir in os.listdir(event_dir):
            sub_dir_path = os.path.join(event_dir, sub_dir)

            if os.path.isdir(sub_dir_path):
                xml_path = os.path.join(sub_dir_path, "Event.xml")

                if os.path.exists(xml_path):
                    try:
                        id_value, type_value = extract_id_type(xml_path)
                        lines.append(f"    ({id_value},{type_value},'2029-01-01 00:00:00.000000','2019-01-01 00:00:00.000000',true),\n")
                    except Exception as e:
                        print(f"Error processing {xml_path}: {e}")

    print("".join(lines)[:-2] + ";\n")
