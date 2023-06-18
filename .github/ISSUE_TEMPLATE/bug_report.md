name: Bug Report
description: File a bug report
title: "[Bug]: "
labels:
  - '[Status] Triage'
assignees:
  - Infiniteblock
body:
  - type: markdown
    attributes:
      value: >-
        NOTE: No support for compiling the project will be provided.

        For help with Matter Overdrive, please use [Matter-Overdrive-Community-Edition/MatterOverdrive-Community-Edition-1.12.2](https://github.com/Matter-Overdrive-Community-Edition/MatterOverdrive-Community-Edition-1.12.2).

        For general Galacticraft help and support visit the [Matter Overdrive: Community Edition Discord](https://discord.gg/hQyAEZV)
  - type: dropdown
    id: forge-version
    attributes:
      label: Forge Version
      options:
        - 14.23.5.2860
        - 14.23.5.2859
        - 14.23.5.2858
        - 14.23.5.2857
        - 14.23.5.2856
        - 14.23.5.2855
        - 14.23.5.2854
        - 14.23.5.2852
        - 14.23.5.2851
        - 14.23.5.2847
        - 14.23.5.2846
        - 14.23.5.2845
        - 14.23.5.2844
        - 14.23.5.2843
        - 14.23.5.2842
        - 14.23.5.2841
        - 14.23.5.2840
        - 14.23.5.2839
        - 14.23.5.2838
        - 14.23.5.2837
        - 14.23.5.2836
        - 14.23.5.2835
        - 14.23.5.2834
        - 14.23.5.2833
        - 14.23.5.2832
        - 14.23.5.2831
        - 14.23.5.2830
        - 14.23.5.2829
        - 14.23.5.2828
        - 14.23.5.2827
        - 14.23.5.2826
        - 14.23.5.2825
        - 14.23.5.2824
        - 14.23.5.2823
        - 14.23.5.2822
        - 14.23.5.2821
        - 14.23.5.2820
        - 14.23.5.2819
        - 14.23.5.2818
        - 14.23.5.2817
        - 14.23.5.2816
        - 14.23.5.2815
        - 14.23.5.2814
        - 14.23.5.2813
        - 14.23.5.2812
        - 14.23.5.2811
        - 14.23.5.2810
        - 14.23.5.2809
        - 14.23.5.2808
        - 14.23.5.2807
        - 14.23.5.2806
        - 14.23.5.2805
        - 14.23.5.2804
        - 14.23.5.2803
        - 14.23.5.2802
        - 14.23.5.2801
        - 14.23.5.2800
    validations:
      required: true
  - type: input
    id: version
    attributes:
      label: Matter Overdrive: Community Edition Version
      placeholder: "0.8.4"
    validations:
      required: true
  - type: textarea
    id: logs
    attributes:
      label: Log or Crash Report
      description: >-
        If you have any logs (`latest.log`, `crash-report<side>-<date>.txt`, etc...)
        Please upload the entire content in the log(s) to any paste site
        *Examples:*
         - [Github Gist](https://gist.github.com/)
         - [Hastebin](https://hastebin.com/)
         - [paste.gg](https://paste.gg/)
         - []
        Please upload your log (latest.log and/or crash report) to [Github
        Gist](https://gist.github.com/) then paste the gist url here, you
        can paste as many links as needed
      placeholder: ''
  - type: textarea
    id: repro
    attributes:
      label: Reproduction steps
      description: How do you trigger this bug? Please walk us through it step by step.
      value: |
        1. Do this
        2. Do that
        3. Crash/Bug
        ...
    validations:
      required: true