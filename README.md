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
    1. sequence of steps see `How to define step` for more details
    2. In addition with a file where you want to initialize certain variables, see `How to define initial variables` for more details
    3. In addition with a file where you want to list variables that should be ultimately displayed after command execution, see `How to define variables that i want to list as output` for more details
    4. In addition with hardcoded data for certain environment combinations, see `How to define hardcoded environment specific data` for more details 
## How to define step
1. Each step must start with some number so that they are sorted properly 
2. Each step in command can be any one of this
    1. Get Request
        1. Should be a file with extention `.get`
        2. Content of the file should be
            1. First line
                1. a plain text representing url template
                2. This can be any `TextTemplate` that has combination of Hardcoded data and variables
                3. e.g.
                    1. First line can look like `${BaseUrl}/posts`
            2. Second Line
                1. It should start with `RequestHeader=` and proceed with json object represented in one line
                2. json object structure should be key value pairs where key represents header to be populated and value represents any `TextTemplate` that result in value
                3. e.g.
                    1. Second line can look like `RequestHeader={"content-type":"application/json","JWT":"${JWT}"}`
            
