String Manipulation
==================
IntelliJ plugin - https://plugins.jetbrains.com/plugin/2162 

Provides actions for text manipulation:

<p>
    <p>
        Switch case:
        <ul>
            <li>Switch style (camelCase, hyphen-lowercase, HYPHEN-UPPERCASE, snake_case, SCREAMING_SNAKE_CASE, dot.case,
                words lowercase, Words Capitalized, PascalCase)
            </li>
            <li>To SCREAMING_SNAKE_CASE (or to camelCase)</li>
            <li>To snake_case (or to camelCase)</li>
            <li>To dot.case (or to camelCase)</li>
            <li>To hyphen-case (or to camelCase)</li>
            <li>To hyphen-case (or to snake_case)</li>
            <li>To camelCase (or to words)</li>
            <li>To PascalCase (or to camelCase)</li>
            <li>Capitalize selected text</li>
            <li>Swap case</li>
        </ul>
        -when nothing is selected, then either nothing or whole line or a particular element is selected - report an issue if you find something to improve, each file type needs its own implementation to work flawlessly.</li>
    </p>
    <p>
        Un/Escape:
        <ul>
            <li>Un/Escape selected Java text</li>
            <li>Un/Escape selected JavaScript text</li>
            <li>Un/Escape selected HTML text</li>
            <li>Un/Escape selected XML text</li>
            <li>Un/Escape selected SQL text</li>
            <li>Un/Escape selected PHP text</li>
            <li>Convert diacritics(accents) to ASCII</li>
            <li>Convert non ASCII to escaped Unicode</li>
            <li>Convert escaped Unicode to String</li>
        </ul>
    </p>
    <p>
        Encode/Decode:
        <ul>
            <li>Encode selected text to MD5 Hex16</li>
            <li>De/Encode selected text as URL</li>
            <li>De/Encode selected text to Base64</li>
        </ul>
    </p>
    <p>
        Increment/Decrement:
        <ul>
            <li>Increment/decrement all numbers found.</li>
            <li>Duplicate line and increment/decrement all numbers found.</li>	
            <li>Create sequence - Keep first number, replace all other by incrementing</li>	
            <li>Increment duplicate numbers</li>	
        </ul>
    </p>
    <p>
        Sort with natural order:
        <ul>
            <li>Reversing order of lines</li>
            <li>Shuffle lines</li>
            <li>Sort case-sensitive A-z</li>
            <li>Sort case-sensitive z-A</li>
            <li>Sort case-insensitive A-Z</li>
            <li>Sort case-insensitive Z-A</li>
            <li>Sort line length asc., desc.</li>
            <li>Sort lines by subselection - only one selection/caret per line is handled</li>
        </ul>
    </p>
    <p>
        Align:
        <ul>
            <li>Format selected text to columns/table by a chosen separator/delimiter</li>
            <li>Align text to left/center/right</li>
        </ul>
    </p>
    <p>
        Filter/Remove/Trim...:
        <ul>
            <li>Grep selected text, All lines not matching input text wil be removed.
                (Does not work in column mode)
            </li>
            <li>Trim selected text</li>
            <li>Trim all spaces in selected text</li>
            <li>Remove all spaces in selected text</li>
            <li>Remove duplicate lines</li>
            <li>Keep only duplicate lines</li>
            <li>Remove empty lines</li>
            <li>Remove all newlines</li>
        </ul>
    </p>
    <p>
        Other:
        <ul>
            <li>Swap Characters/Selections/Lines/Tokens</li>
            <li>Switch file path separators: Windows&lt;-&gt;UNIX</li>
        </ul>
    </p>
    <p>Actions are available under Edit menu, or via the shortcut "alt M" and "alt shift M".
        You can setup your own shortcuts for better usability.
    </p>
</p>