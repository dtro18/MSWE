import mysql.connector
from mysql.connector import Error

# Define connection parameters
db_config = {
    'host': 'localhost',         # or the IP address if hosted remotely
    'user': 'root',     # replace with your MySQL username
    'password': 'Stegosaurus9001$', # replace with your MySQL password
    'database': 'university'     # replace with your database name
}

connection = mysql.connector.connect(**db_config)

cursor = connection.cursor()

# Adds a student to student database.
def add_student(first, last):
    cursor.execute("INSERT INTO students (student_first_name, student_last_name) VALUES (%s, %s)", (first, last))
    connection.commit()
# Adds a course to the courses database
def add_course(name, description):
    cursor.execute("INSERT INTO courses (course_name, course_description) VALUES (%s, %s)", (name, description))
    connection.commit()

def get_course_id(name, description):
    cursor.execute("SELECT course_id FROM courses WHERE course_name = (%s) AND course_description = (%s)", (name, description))
    result = cursor.fetchone()
    if result:
        course_id = result[0]
        return course_id
    else:
        print("No course found with the specified name and description.")

def print_menu():
    print("Main Menu")
    print("[1] Enroll student into university")
    print("[2] Add new course")
    print("[3] Enroll student in course")
    print("[4] Display students in a course")
    print("[5] Display a student's courses")
    print("[6] Display a student's course schedule on a given day")
    print("[7] Quit")

# Takes in user input as a string and returns a validated integer
def collect_user_input():
    menu_choice = input("Enter 1-7 ")
    if menu_choice not in "1234567":
        print("Invalid input")
    else:
        return int(menu_choice)

# Collects course time/dates and inserts into db
def collect_course_schedule(course_id):
    int_to_weekday = {1: "Monday", 2: "Tuesday", 3: "Wednesday", 4: "Thursday", 5: "Friday"}
    days = []
    # Takes the form "123" or "24"
    print("Enter weekdays that the course meets (1 = Monday, 2 = Tuesday, etc.).")
    print("Multiple days can be entered at the same time (e.g. 135 is MWF)")           
    days_string = input()
    for c in days_string:
        c = int(c)
        if 1 <= c <= 5:
            days.append(int_to_weekday[c])
            print(days)
        else:
            print("Invalid input")
            return   
         
    course_start_time = {'hours': '00', 'minutes': '00', 'seconds': '00'}
    print("Start time")
    course_start_time['hours'] = input("Enter hours (24h format, e.g. 11 for 11AM)").zfill(2)
    course_start_time['minutes'] = input("Enter minutes (e.g. 30 for XX:30)").zfill(2)
    start_time_string = f"{course_start_time['hours']}:{course_start_time['minutes']}:{course_start_time['seconds']}"

    course_end_time = {'hours': '00', 'minutes': '00', 'seconds': '00'}
    print("End time")
    course_end_time['hours'] = input("Enter hours (24h format, e.g. 11 for 11AM)").zfill(2)
    course_end_time['minutes'] = input("Enter minutes (e.g. 30 for XX:30)").zfill(2)
    end_time_string = f"{course_end_time['hours']}:{course_end_time['minutes']}:{course_end_time['seconds']}"
    
    for day in days_string:
        cursor.execute("INSERT INTO course_schedules (course_id, day_of_week, start_time, end_time) VALUES (%s, %s, %s, %s)", 
                       (course_id, day, start_time_string, end_time_string))
        connection.commit()
# Enrolls a student in a course. Expects ints.
def enroll_student_in_course(student_id, course_id):
    cursor.execute("INSERT INTO enrollment (student_id, course_id) VALUES (%s, %s)", (student_id, course_id))
    connection.commit()

def display_course_students(course_id):
    cursor.execute("SELECT course_name, course_description "
                   "FROM university.courses "
                   "WHERE course_id = %s ", (course_id,))
    rows = cursor.fetchone()
    course_name = rows[0]
    course_description = rows[1]

    cursor.execute("SELECT s.student_first_name, s.student_last_name "
                    "FROM university.students s "
                    "JOIN university.enrollment e "
                    "ON s.student_id = e.student_id "
                    "WHERE e.course_id = %s", (course_id,))
    
    rows = cursor.fetchall()
    print(course_name)
    print(course_description)
    print("Enrolled students: ")
    # Print each row
    for row in rows:
        print(row[0], row[1])

def display_student_courses(student_id):
    cursor.execute("SELECT student_first_name, student_last_name "
                   "FROM university.students "
                   "WHERE student_id = %s", (student_id,))
    rows = cursor.fetchone()
    student_first_name = rows[0]
    student_last_name = rows[1]

    cursor.execute("SELECT c.course_name "
                   "FROM university.courses c "
                   "JOIN university.enrollment e "
                   "ON c.course_id = e.course_id "
                   "WHERE e.student_id = %s", (student_id,))

    rows = cursor.fetchall()
    print(student_first_name + " " + student_last_name)
    print("Courses: ")
    # Print each row
    for row in rows:
        print(row[0])
    
def display_student_schedule_day(student_id, weekday):
    cursor.execute("""
                    SELECT s.student_first_name, 
                    s.student_last_name, 
                    c.course_name, 
                    cs.day_of_week, 
                    DATE_FORMAT(cs.start_time, '%h:%i %p') AS start_time,
                    DATE_FORMAT(cs.end_time, '%h:%i %p') AS end_time
                    FROM university.students s
                    JOIN university.enrollment e
                    ON s.student_id = e.student_id
                    JOIN university.courses c
                    ON e.course_id = c.course_id
                    JOIN university.course_schedules cs
                    ON c.course_id = cs.course_id
                    WHERE cs.day_of_week = %s AND s.student_id = %s
                   """, (weekday, student_id))
    rows = cursor.fetchall()
    student_name = rows[0][0] + " " + rows[0][1]
    day_of_week = rows[0][3]
    print(f"Schedule for {student_name} on {day_of_week}")
    for row in rows:
        print(row[2] + " " + row[4] + " - " + row[5])
    
# Case functions

# Enroll student in university
def case_1():
    first = input("Enter first name: ")
    last = input("Enter last name: ")
    add_student(first, last)
# Adds course to courses and course_schedule tables
def case_2():
    name = input("Enter course name: ")
    description = input("Enter course description: ")
    add_course(name, description)
    collect_course_schedule(get_course_id(name, description))

# Enroll student in course
def case_3():
    student_id = int(input("Enter student ID: "))
    course_id = int(input("Enter course ID: "))
    enroll_student_in_course(student_id, course_id)

# Show which students are in a given course
def case_4():
    course_id = int(input("Enter course ID: "))
    display_course_students(course_id)

# Show which courses a given student is enrolled in
def case_5():
    student_id = int(input("Enter student ID: "))
    display_student_courses(student_id)
    
# Display a student's schedule on a given day
def case_6():
    student_id = int(input("Enter student ID: "))
    int_to_weekday = {1: "Monday", 2: "Tuesday", 3: "Wednesday", 4: "Thursday", 5: "Friday"}
    weekday = int_to_weekday[int(input("Enter day (1 = Monday, 2 = Tuesday, etc.): "))]
    display_student_schedule_day(student_id, weekday)


# Main Menu Loop

while True:
    print_menu()
    user_input = collect_user_input()

    if user_input == 1:
        case_1()
    elif user_input == 2:
        case_2()
    elif user_input == 3:
        case_3()
    elif user_input == 4:
        case_4()
    elif user_input == 5:
        case_5()
    elif user_input == 6:
        case_6()
    elif user_input == 7:
        break




