# Creating executable jar
1. To Create Executable jar file run command
`gradle clean build`
2. Grab cli-0.0.1.jar from cli/build/libs directory
3. Place this file into any directory where you want to configure your data journeys

# Configuring Tool
1. Create folder named `config` in same directory where you have placed above jar
2. Inside `config` create two folders named `commands` and `env`
    1. More about `env`
        1. This is the place where you want to create different environments
        2. for each environment create file directly with the name of environment
            1. e.g. 
                1. If you have `dev` environments for which urls differ create file named `dev`
                2. Similarly if you have `qa` environment for which url differs create file named `qa`
        3. Each environment file should have simple `name=value` formatted configurations
            1. name can be name of any variable, see `Variable naming conventions` for more details
            2. value can be any hardcoded value or a `TextTemple` that can result into value, see `TextTemplate` section for more details
    2. More about `commands`
        1. Each folder created in commands directory is your separate command or data journey that you want to configure
        2. Name each of this folders wisely, as this names will be directly treated as command names which will be displayed as options or choosed based on filters
        3. See `Configuring Commands/Journeys` for how to configure commands
        
# Configuring Commands/Journeys
1. Each command is 
    1. Sequence of steps see `How to define step` for more details
    2. And a file where you want to initialize certain variables, see `How to define initial variables` for more details
    3. And a file where you want to list variables that should be ultimately displayed after command execution, see `How to define variables that i want to list as output` for more details
    4. And hardcoded data for certain environment combinations, see `How to define hardcoded environment specific data` for more details
     
## How to define step
1. Each step must start with some number so that they are sorted properly 
2. Each step in command can be any one of this
    1. Get Request Unit - Refer `Defining Get Request` for more details
    2. Post Request Unit - Refer `Defining Post Request` for more details
    3. Patch Request Unit - Refer `Defining Patch Request` for more details
    4. Put Request Unit - Refer `Defining Put Request` for more details
    5. Delete Request Unit - Refer `Defining Delete Request` for more details
    6. Static Loop Unit - Refer `Defining Static Loop` for more details
    7. Block Unit - Refer `Defining Block Unit` for more details
    8. Poll Unit - Refer `Defining Poll Unit` for more details
    8. Journey Unit - Refer `Defining Journey Unit` for more details
    
### Defining Get Request
1. By This step yu can send Get Request to any url with templated inputs that you specify
2. Create file with extention `.get`
3. Content of the file should be
    1. First line
        1. This line is all about URL to which you want to send request
        1. So it should be a plain text representing url template
        2. This can be any `TextTemplate` that has combination of Hardcoded data and variables, Refer `Defining TextTemplate` for more details
        3. e.g.
            1. This line can look like `${BaseUrl}/posts`
    2. Second Line
        1. This line is all about Headers That you want to send with Request
        2. It should start with `RequestHeader=` and proceed with json object represented in one line
        3. json object structure should be key value pairs where key represents header to be populated and value represents any `TextTemplate` that result in value
        4. e.g.
            1. This line can look like `RequestHeader={"content-type":"application/json","JWT":"${JWT}"}`
        5. Your should leave this line with empty json object `{}` in case you dont have any Request headers
    3. Third Line
        1. This line is all about response that you want to process and capture data from.
        2. It should start with `Response=` and proceed with any valid `ExtractableTemplate` refer `Defining ExtractableTemplate` for more details
        3. e.g.
            1. This line can look like `Response={"result":${Result}}`
    4. Forth Line
        1. This line is all about data to be captured from Response Headers
        2. It should start with `ResponseHeader=` and proceed with json object represented in one line
        3. json object structure should be key value pairs where key represents header to be captured and value represents any name of the variable in which it needs to be captured
        4. e.g.
            1. This line can look like `RequestHeader={"JWT":"CapturedJWT"}`
        5. Your should leave Forth line with empty json object `{}` in case you dont want to capture anything from response headers
    5. Fifth Line (optional)
        1. This line is optional and represents wait time in milliseconds that journey processor should wait before executing next Step.
        2. It should start with `Wait=` and proceed with any integer
    
### Defining Post Request
1. By This step yu can send Post Request to any url with templated inputs that you specify
2. Create file with extention `.post`
3. Content of the file should be
    1. First line
        1. This line is all about URL to which you want to send request
        1. So it should be a plain text representing url template
        2. This can be any `TextTemplate` that has combination of Hardcoded data and variables, Refer `Defining TextTemplate` for more details
        3. e.g.
            1. This line can look like `${BaseUrl}/posts`
    3. Second Line
        1. This line is all about Request that you want to send
        2. It should start with `Body=` and proceed with any valid `Template` refer `Defining Template` for more details
        3. e.g.
            1. This line can look like `Body={"name":${Name},"age":${Age}}`
        4. You should leave `Body=` in case you dont have anu body to be passed 
    2. Third Line
        1. This line is all about Headers That you want to send with Request
        2. It should start with `RequestHeader=` and proceed with json object represented in one line
        3. json object structure should be key value pairs where key represents header to be populated and value represents any `TextTemplate` that result in value
        4. e.g.
            1. This line can look like `RequestHeader={"content-type":"application/json","JWT":"${JWT}"}`
        5. Your should leave This line with empty json object `{}` in case you dont have any Request headers
    3. Fourth Line
        1. This line is all about response that you want to process and capture data from.
        2. It should start with `Response=` and process with any valid `ExtractableTemplate` refer `Defining ExtractableTemplate` for more details
        3. e.g.
            1. This line can look like `Response={"result":${Result}}`
    4. Fifth Line
        1. This line is all about data to be captured from Response Headers
        2. It should start with `ResponseHeader=` and proceed with json object represented in one line
        3. json object structure should be key value pairs where key represents header to be captured and value represents any name of the variable in which it needs to be captured
        4. e.g.
            1. This line can look like `RequestHeader={"JWT":"CapturedJWT"}`
        5. Your should leave This line with empty json object `{}` in case you dont want to capture anything from response headers
    5. Sixth Line (optional)
        1. This line is optional and represents wait time in milliseconds that journey processor should wait before executing next Step.
        2. It should start with `Wait=` and proceed with any integer
    
### Defining Patch Request
1. By This step yu can send Patch Request to any url with templated inputs that you specify
2. Create file with extention `.patch`
3. Content of the file should be
    1. First line
        1. This line is all about URL to which you want to send request
        1. So it should be a plain text representing url template
        2. This can be any `TextTemplate` that has combination of Hardcoded data and variables, Refer `Defining TextTemplate` for more details
        3. e.g.
            1. This line can look like `${BaseUrl}/posts`
    3. Second Line
        1. This line is all about Request that you want to send
        2. It should start with `Body=` and proceed with any valid `Template` refer `Defining Template` for more details
        3. e.g.
            1. This line can look like `Body={"name":${Name},"age":${Age}}`
        4. You should leave `Body=` in case you dont have anu body to be passed
    2. Third Line
        1. This line is all about Headers That you want to send with Request
        2. It should start with `RequestHeader=` and proceed with json object represented in one line
        3. json object structure should be key value pairs where key represents header to be populated and value represents any `TextTemplate` that result in value
        4. e.g.
            1. This line can look like `RequestHeader={"content-type":"application/json","JWT":"${JWT}"}`
        5. Your should leave This line with empty json object `{}` in case you dont have any Request headers
    3. Fourth Line
        1. This line is all about response that you want to process and capture data from.
        2. It should start with `Response=` and process with any valid `ExtractableTemplate` refer `Defining ExtractableTemplate` for more details
        3. e.g.
            1. This line can look like `Response={"result":${Result}}`
    4. Fifth Line
        1. This line is all about data to be captured from Response Headers
        2. It should start with `ResponseHeader=` and proceed with json object represented in one line
        3. json object structure should be key value pairs where key represents header to be captured and value represents any name of the variable in which it needs to be captured
        4. e.g.
            1. This line can look like `RequestHeader={"JWT":"CapturedJWT"}`
        5. Your should leave This line with empty json object `{}` in case you dont want to capture anything from response headers
    5. Sixth Line (optional)
        1. This line is optional and represents wait time in milliseconds that journey processor should wait before executing next Step.
        2. It should start with `Wait=` and proceed with any integer

### Defining Put Request
1. By This step yu can send Put Request to any url with templated inputs that you specify
2. Create file with extention `.put`
3. Content of the file should be
    1. First line
        1. This line is all about URL to which you want to send request
        1. So it should be a plain text representing url template
        2. This can be any `TextTemplate` that has combination of Hardcoded data and variables, Refer `Defining TextTemplate` for more details
        3. e.g.
            1. This line can look like `${BaseUrl}/posts`
    3. Second Line
        1. This line is all about Request that you want to send
        2. It should start with `Body=` and proceed with any valid `Template` refer `Defining Template` for more details
        3. e.g.
            1. This line can look like `Body={"name":${Name},"age":${Age}}`
        4. You should leave `Body=` in case you dont have anu body to be passed
    2. Third Line
        1. This line is all about Headers That you want to send with Request
        2. It should start with `RequestHeader=` and proceed with json object represented in one line
        3. json object structure should be key value pairs where key represents header to be populated and value represents any `TextTemplate` that result in value
        4. e.g.
            1. This line can look like `RequestHeader={"content-type":"application/json","JWT":"${JWT}"}`
        5. Your should leave This line with empty json object `{}` in case you dont have any Request headers
    3. Fourth Line
        1. This line is all about response that you want to process and capture data from.
        2. It should start with `Response=` and process with any valid `ExtractableTemplate` refer `Defining ExtractableTemplate` for more details
        3. e.g.
            1. This line can look like `Response={"result":${Result}}`
    4. Fifth Line
        1. This line is all about data to be captured from Response Headers
        2. It should start with `ResponseHeader=` and proceed with json object represented in one line
        3. json object structure should be key value pairs where key represents header to be captured and value represents any name of the variable in which it needs to be captured
        4. e.g.
            1. This line can look like `RequestHeader={"JWT":"CapturedJWT"}`
        5. Your should leave This line with empty json object `{}` in case you dont want to capture anything from response headers
    5. Sixth Line (optional)
        1. This line is optional and represents wait time in milliseconds that journey processor should wait before executing next Step.
        2. It should start with `Wait=` and proceed with any integer

### Defining Delete Request
1. By This step yu can send Delete Request to any url with templated inputs that you specify
2. Create file with extention `.delete`
3. Content of the file should be
    1. First line
        1. This line is all about URL to which you want to send request
        1. So it should be a plain text representing url template
        2. This can be any `TextTemplate` that has combination of Hardcoded data and variables, Refer `Defining TextTemplate` for more details
        3. e.g.
            1. This line can look like `${BaseUrl}/posts`
    3. Second Line
        1. This line is all about Request that you want to send
        2. It should start with `Body=` and proceed with any valid `Template` refer `Defining Template` for more details
        3. e.g.
            1. This line can look like `Body={"name":${Name},"age":${Age}}`
        4. You should leave `Body=` in case you dont have anu body to be passed
    2. Third Line
        1. This line is all about Headers That you want to send with Request
        2. It should start with `RequestHeader=` and proceed with json object represented in one line
        3. json object structure should be key value pairs where key represents header to be populated and value represents any `TextTemplate` that result in value
        4. e.g.
            1. This line can look like `RequestHeader={"content-type":"application/json","JWT":"${JWT}"}`
        5. Your should leave This line with empty json object `{}` in case you dont have any Request headers
    3. Fourth Line
        1. This line is all about response that you want to process and capture data from.
        2. It should start with `Response=` and process with any valid `ExtractableTemplate` refer `Defining ExtractableTemplate` for more details
        3. e.g.
            1. This line can look like `Response={"result":${Result}}`
    4. Fifth Line
        1. This line is all about data to be captured from Response Headers
        2. It should start with `ResponseHeader=` and proceed with json object represented in one line
        3. json object structure should be key value pairs where key represents header to be captured and value represents any name of the variable in which it needs to be captured
        4. e.g.
            1. This line can look like `RequestHeader={"JWT":"CapturedJWT"}`
        5. Your should leave This line with empty json object `{}` in case you dont want to capture anything from response headers
    5. Sixth Line (optional)
        1. This line is optional and represents wait time in milliseconds that journey processor should wait before executing next Step.
        2. It should start with `Wait=` and proceed with any integer

### Defining Static Loop
1. By This step you can loop over group of steps specific number of times 
2. Create Folder whose name ends with `.loop`
3. Inside This Folder create plain text file `.info` contents of the `.info` file should be as follow
    1. This file is all about information for loop unit all its Lines should be as follow
    2. First Line
        1. This line is all about counter variable value
        2. It should start with `Counter=` and proceed with name of the variable in which you want to store counter value
        3. e.g.
            1. This line can look like `Counter=counter`
            2. by this variable named counter will be saving the value of current loop count
    3. Second Line
        1. This line is all about number of times you want to execute contents of the loop
        2. It should start with `Times=` and proceed with any `TextTemplate` that results in integer see `Defining TextTemplate` for more details
        3. e.g
            1. This line can look like `Times=${Value-2}`
    4. Third Line (optional)
       1. This line is optional and represents wait time in milliseconds that journey processor should wait before executing next Step.
       2. It should start with `Wait=` and proceed with any integer
4. Create any steps within this file same way you create step in Command, all this steps will be executed sequentially per iteration
### Defining Block Unit
1. By This step you can loop over group of steps specific number of times 
2. Create Folder whose name ends with `.loop`
3. Inside This Folder create plain text file `.info` contents of the `.info` file should be as follow
    1. This file is all about information for loop unit all its Lines should be as follow
    2. First Line
        1. This line is all about counter variable value
        2. It should start with `Counter=` and proceed with name of the variable in which you want to store counter value
        3. e.g.
            1. This line can look like `Counter=counter`
            2. by this variable named counter will be saving the value of current loop count
    3. Second Line
        1. This line is all about number of times you want to execute contents of the loop
        2. It should start with `Times=` and proceed with any `TextTemplate` that results in integer see `Defining TextTemplate` for more details
        3. e.g
            1. This line can look like `Times=${Value-2}`
    4. Third Line (optional)
       1. This line is optional and represents wait time in milliseconds that journey processor should wait before executing next Step.
       2. It should start with `Wait=` and proceed with any integer
4. Create any steps within this file same way you create step in Command, all this steps will be executed sequentially per iteration
### Defining Poll Unit
1. By This step you can loop over group of steps specific number of times 
2. Create Folder whose name ends with `.loop`
3. Inside This Folder create plain text file `.info` contents of the `.info` file should be as follow
    1. This file is all about information for loop unit all its Lines should be as follow
    2. First Line
        1. This line is all about counter variable value
        2. It should start with `Counter=` and proceed with name of the variable in which you want to store counter value
        3. e.g.
            1. This line can look like `Counter=counter`
            2. by this variable named counter will be saving the value of current loop count
    3. Second Line
        1. This line is all about number of times you want to execute contents of the loop
        2. It should start with `Times=` and proceed with any `TextTemplate` that results in integer see `Defining TextTemplate` for more details
        3. e.g
            1. This line can look like `Times=${Value-2}`
    4. Third Line (optional)
       1. This line is optional and represents wait time in milliseconds that journey processor should wait before executing next Step.
       2. It should start with `Wait=` and proceed with any integer
4. Create any steps within this file same way you create step in Command, all this steps will be executed sequentially per iteration
### Defining Journey Unit
        
            
