<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Test Automation Framework</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <link href="css/systemBar.min.css" rel="stylesheet" type="text/css">
    <link href="css/assets.min.css" rel="stylesheet" type="text/css">
    <link href="css/font-awesome.min.css" rel="stylesheet" type="text/css">
    <link href="css/master.css" rel="stylesheet" type="text/css">
</head>
<body class="eaTafLanding">
    <header class="eaTafLanding">
        <div class="ebSystemBar">
            <div class="eaTafLanding-wrapper">
                <div class="ebSystemBar-logo"></div>
                <h1 class="eaTafLanding-title">Test Automation Framework</h1>
                <p class="eaTafLanding-subtitle">
                    TAF ensures the maintainability and scalability of your software<br>
                    with automated feature and performance testing tailored to Ericsson technology.
                </p>
            </div>
        </div>
    </header>
    <div class="eaTafLanding-body eaTafLanding-wrapper">
        <div class="eaTafLanding-row">
            <div class="eaTafLanding-col eaTafLanding-col_4">
                <div class="eaTafLanding-box">
                    <h2 class="eaTafLanding-col-title">
                        <i class="fa fa-book"></i>
                        API documentation versions
                    </h2>
                    <ul class="eaTafLanding-links">
			<li><a href="apidocs/Latest">Latest</li><br/>
                        <?php
                        if ($handle = opendir('apidocs')) {
                            while (false !== ($entry = readdir($handle))) {
                                if ($entry != "." && $entry != ".." && $entry != "snapshot" && $entry != "Latest") {
                                    echo "<li><a href=\"apidocs/$entry\">$entry</li><br/>";
                                }
                            }
                            closedir($handle);
                        }
                        ?>
                    </ul>
                </div>
            </div>

        </div>
    </div>
</body>
</html>
