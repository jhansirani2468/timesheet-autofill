import sys, json, os
from ai_gateway_client import generate_bearer_token, invoke_bedrock_model

def build_prompt(issues):
    template = open("prompt_templates/timesheet_prompt.txt").read()
    lines = [template, "\nIssues:\n"]
    for it in issues:
        lines.append(f"- {it.get('key')}: {it.get('summary','')}\n")
        desc = it.get('description','')
        if desc:
            lines.append("  desc: " + " ".join(desc.split()) + "\n")
    return "\n".join(lines)

def main():
    if len(sys.argv) < 2:
        print("[]")
        return
    issues_path = sys.argv[1]
    with open(issues_path, "r") as f:
        issues = json.load(f)
    prompt = build_prompt(issues)
    token = generate_bearer_token()
    resp = invoke_bedrock_model(token, prompt)

    # Best-effort extract text
    text = None
    if isinstance(resp, dict):
        if "choices" in resp and len(resp["choices"])>0:
            c = resp["choices"][0]
            text = c.get("text") or c.get("message") or c.get("output")
        for key in ("result","output","text","generated_text"):
            if key in resp:
                text = resp[key]
                break
    if text is None:
        # maybe the gateway returns array directly
        try:
            print(json.dumps(resp))
            return
        except:
            print("[]")
            return

    # try parse text as json
    try:
        parsed = json.loads(text)
        print(json.dumps(parsed))
        return
    except Exception:
        # extract bracketed array
        s = text.find('[')
        e = text.rfind(']')
        if s!=-1 and e> s:
            try:
                arr = json.loads(text[s:e+1])
                print(json.dumps(arr))
                return
            except:
                pass

    # fallback
    print(json.dumps({"raw": resp}))

if __name__ == "__main__":
    main()
