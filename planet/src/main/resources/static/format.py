from pathlib import Path
import json

for f in Path(__file__).parent.glob('*.json'):
    obj = json.loads(f.read_text('utf-8'))
    blacklist = ['end_date', 'start_date']
    obj = [{k: v for k, v in o.items() if k not in blacklist} for o in obj]
    f.write_text('[\n' + ',\n'.join(json.dumps(o, ensure_ascii=False) for o in obj) + '\n]\n', 'utf-8')
