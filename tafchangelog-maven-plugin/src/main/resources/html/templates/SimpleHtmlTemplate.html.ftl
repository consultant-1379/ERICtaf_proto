<!DOCTYPE html>
<html>
<head>
    <meta content="HTML Tidy for HTML5 (experimental) for Windows https://github.com/w3c/tidy-html5/tree/c63cc39" name="generator" />
    <meta charset="UTF-8" />
    <title>${project} Release and Change Log</title>
    <style type="text/css">
        html {
            font-family: "Helvetica Neue", Helvetica, Arial, sans-serif;
            color: #002561;
        }
        body{
            margin: 0px
        }
        .changelog-body{
            margin-left: 1%;
            padding-top: 3rem;
        }
        td {
            border: 1px solid #CCCCCC;
        }
        td.date, .author, .committer {
            white-space: nowrap;
        }
        tag td {
            border-width: 0px;
            font-weight: bold;
            padding-top: 30px;
            padding-bottom: 7px;
        }
        a
        {
            color: #0066b3;
        }
        a:hover {
            color: #002561;
        }
        a, a:visited {
            text-decoration: none;
        }
        a:-webkit-any-link {
            cursor: auto;
        }
        li
        {
            list-style-type: none;
            font-size: 14px;
            color: #333;
            background-image: url(images/handarrow.png);
            background-repeat: no-repeat;
            background-position: 20px top;
            padding-left: 40px;
        }
        .tagdate
        {
            font-size:14px;
            color: #838383;
        }
        .tag
        {
            font-weight: bold;
        }
        .major
        {
            display: block;

        }
        .minor
        {
            display: block;
        }
        .patch
        {
            display: block;
        }
        .logtitle
        {
            font-weight: 300;
            font-size: 24px;
        }
        .versionRadioBtn
        {
            font-weight: 100;
            font-size: 15px;
        }
        .eaContainer-SystemBar {
            font-family: Arial, Helvetica, sans-serif;
            font-size: 1.2rem;
            position: fixed;
            top:0;
            width: 100%;
            height: 40px;
            z-index: 1500;
            border-top: 4px solid #0066b3;
            white-space: nowrap;
            background: #f0f0f0;
            background: -webkit-gradient(linear, left top, left bottom, color-stop(0, #ffffff), color-stop(1, #f0f0f0));
            background: -ms-linear-gradient(top, #ffffff, #f0f0f0);
            background: -o-linear-gradient(center top, #ffffff 0%, #f0f0f0 100%);
            background: -webkit-linear-gradient(center top, #ffffff 0%, #f0f0f0 100%);
            background: -moz-linear-gradient(center top, #ffffff 0%, #f0f0f0 100%);
            background: linear-gradient(center top, #ffffff 0%, #f0f0f0 100%);
            -webkit-box-shadow: 0 2px 2px #D2D2D2;
            -moz-box-shadow: 0 2px 2px #D2D2D2;
            box-shadow: 0 2px 2px #D2D2D2;
        }
        .eaContainer-SystemBar::before {
            content: "";
            position: absolute;
            width: 100%;
            height: 4px;
            font-size: 0;
            top: -4px;
            left: 0;
            background: #0066b3;
            background: -o-linear-gradient(left, #a2c517 10%, #009046 30%, #0082b6 50%, #151f77 70%, #db0050 100%);
            background: -moz-linear-gradient(left, #a2c517 10%, #009046 30%, #0082b6 50%, #151f77 70%, #db0050 100%);
            background: -webkit-linear-gradient(left, #a2c517 10%, #009046 30%, #0082b6 50%, #151f77 70%, #db0050 100%);
            background: -ms-linear-gradient(left, #a2c517 10%, #009046 30%, #0082b6 50%, #151f77 70%, #db0050 100%);
            background: -webkit-gradient(linear, left top, right top, color-stop(0.1, #a2c517), color-stop(0.3, #009046), color-stop(0.5, #0082b6), color-stop(0.7, #151f77), color-stop(1, #db0050));
            background: linear-gradient(left, #a2c517 10%, #009046 30%, #0082b6 50%, #151f77 70%, #db0050 100%);
        }
        .eaContainer-SystemBar-name {
            display: inline-block;
            padding: 0 5px 0 0;
            margin: 0;
            font-size: 1.4em;
            line-height: 1.2em;
            color: #58585A;
            max-width: 100%;
            overflow: hidden;
            white-space: nowrap;
            text-overflow: ellipsis;
            position: absolute;
            left: 40px;
            right: 0;
            top: 0;
            bottom: 0;
        }
        .eaContainer-SystemBar-nameValue {
            font-size: 16.8px;
            color: #58585A;
        }
        .eaContainer-SystemBar-nameValue:hover {
            color: #58585A;
        }
        .eaContainer-SystemBar-nameValue:focus {
            text-decoration: none;
            outline: none;
        }
        .eaContainer-SystemBar-logoAnchor {
            outline: none;
        }
        .eaContainer-SystemBar-logo {
            height: 40px;
            display: inline-block;
            background: url('images/elogo.svg') no-repeat center center transparent;
            width: 40px;
            background-size: 20px 20px;
        }
        .eaContainer-SystemBar-left {
            overflow: hidden;
            white-space: nowrap;
            position: relative;
        }
        a.eaContainer-SystemBar-nameValue:hover {
            text-decoration: none !important;
        }
        @media screen and (max-width: 500px) {
        .eaContainer-SystemBar {
            z-index: 1000;
        }
        }
    </style>
    <script>
        function handleClick(version) {
            var versionTags = document.getElementsByClassName('version');
            for(i=0; i<versionTags.length; i++) {
                if(versionTags[i].classList.contains(version.value))
                     versionTags[i].style.display = "block";
                else
                     versionTags[i].style.display = "none";
            }
        }
    </script>
</head>
<body>
<div class="eaContainer-SystemBar">
    <div class="eaContainer-SystemBar-left">
        <a href="#changelog" tabindex="-1" class="eaContainer-SystemBar-logoAnchor"><div class="eaContainer-SystemBar-logo"></div></a>
        <div class="eaContainer-SystemBar-name"><a href="#changelog" tabindex="-1" class="eaContainer-SystemBar-nameValue">${project}</a></div>
    </div>
</div>

<div class="changelog-body">
<#if (releaseNotesLink??)>
<h2 class="logtitle">${project} Release and Change Log</h2>

<h3 class="logtitle">Release notes</h3>
<p>
    <a href="${releaseNotesLink}" target="_blank">${releaseNotesLink}</a>
</p>
</#if>

<h3 class="logtitle">Change Log
<span class ="versionRadioBtn">
(
<input type="radio" name="editList" onclick="handleClick(this);" value="major">Major</input>
<input class="versionRadioBtn" type="radio" name="editList" onclick="handleClick(this);" value="minor">Minor</input>
<input class="versionRadioBtn" type="radio" name="editList" onclick="handleClick(this);" value="build" checked="checked">Build</input>
)
</span>
</h3>
<div class="changelog">

<#if (lastCommits?size != 0)>
<#list lastCommits as commit>
   <li>${commit.message}</li>
</#list>
</#if>

<#list revisionHistory.allReleaseTags as tag>
    <#if tag.versionType?matches("MAJOR")>
        <hr class="version major minor build"/>
        <p class="version major minor build">
    <#elseif tag.versionType?matches("MINOR")>
        <hr class="version minor build"/>
        <p class="version minor build">
    <#else>
        <hr class="version build"/>
        <p class="version build">
    </#if>
    <span class="tag">${tag.htmlContent}</span>
    <em><span class="tagdate">(${tag.commitDate})</span></em>
    </p>

    <#assign commits = revisionHistory.getReleaseCommits(tag)/>
    <#if commits??>
        <#list commits as commit>
            <li>${commit.message}</li>
        </#list>
     </#if>

</#list>
</div>
</div>
</body>
</html>

