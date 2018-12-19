# returnsCalculator
Calculates the returns of Vanguard ETFs bought on the ASX, including any additional shares accumulated through the distribution reinvestment plan

1. Parses an input file that describes an individual's purchase history of Vanguard ETFs.
2. Uses the ASX API to get current price information for the relevant ETFs, and scrapes the Vanguard website for the distribution information.
3. Based on the timing of the ETF purchases and any distribution reinvestment plan events, calculates the value of the individual's current holdings.

See the /exampleInputs/ directory for the correct format for input text files.

Note: currently only supports VAS, VGS and VGE ETFs.
